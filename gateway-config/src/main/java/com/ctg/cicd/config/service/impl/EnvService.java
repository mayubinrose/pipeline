package com.ctg.cicd.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctg.cicd.common.base.CommonConstants;
import com.ctg.cicd.common.constant.ResourceConstants;
import com.ctg.cicd.common.exception.BusinessException;
import com.ctg.cicd.common.model.dto.AppDeployCountDTO;
import com.ctg.cicd.common.model.dto.EnvDTO;
import com.ctg.cicd.common.model.dto.OperateReturnDTO;
import com.ctg.cicd.common.model.dto.SaveEntityReturnDTO;
import com.ctg.cicd.common.model.vo.EnvResourceVO;
import com.ctg.cicd.common.model.vo.EnvVO;
import com.ctg.cicd.config.dao.EnvDao;
import com.ctg.cicd.config.entity.Env;
import com.ctg.cicd.config.entity.EnvResource;
import com.ctg.cicd.config.manager.DictManager;
import com.ctg.cicd.config.service.IEnvResourceService;
import com.ctg.cicd.config.service.IEnvService;
import com.ctg.cloud.paascommon.entity.UserInfo;
import com.ctg.cloud.paascommon.utils.SecurityUtils;
import com.ctg.eadp.common.constant.AppDeployInstConstants;
import com.ctg.eadp.common.util.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.ctg.eadp.common.exception.BusinessException.BASE_RESOURCE_NOT_NULL;
import static com.ctg.eadp.common.exception.BusinessException.K8S_RESOURCE_EXIST;

/**
 * <p>
 * 环境表 服务实现类
 * </p>
 *
 * @author hel
 * @since 2023-05-30
 */
@Slf4j
@Service
public class EnvService extends ServiceImpl<EnvDao, Env> implements IEnvService {

    private static final String FIELD_BIZ_NAME = "envName";

    private static final String ORDER_DESC = "DESC";

    @Autowired
    private EnvDao envDao;
    @Autowired
    private IEnvResourceService envResourceService;


    @Override
    public SaveEntityReturnDTO createEnv(EnvVO envVO, String userName, Long tenantId) {

        Env env = selectByEnvCode(envVO.getEnvCode(),tenantId);
        if (null != env){
            throw BusinessException.DUPLICATED_ENV_CODE.exception(envVO.getEnvCode());
        }
        env = saveEnvInfo(envVO,userName,tenantId);
        saveEnvResource(envVO, env, userName);
        return new SaveEntityReturnDTO(env.getId(),env.getEnvUuid());
    }

    @Override
    public OperateReturnDTO updateEnv(Long id, EnvVO envVo,String userName) {
        checkBaseEnvResource(id, envVo.getBaseEnvResource());

        envResourceService.deleteResourceByEnvId(id);

        updateEnvInfo(id,envVo,userName);
        Env env = getById(id);
        saveEnvResource(envVo, env, userName);

        return new OperateReturnDTO(Boolean.TRUE);
    }

    @Override
    public List<EnvVO> listEnvByNodeId(Long nodeId, String envName, String envType, String sort, String order, Long userId, Long tenantId) {
        List<EnvDTO> envDTOList = envDao.listByNodeIdAndEnvName(nodeId, envName, envType, tenantId);
        if (CollectionUtils.isEmpty(envDTOList)) {
            return Collections.emptyList();
        }

        List<Long> idSet = envDTOList.stream().map(EnvDTO::getId).collect(Collectors.toList());

        Map<Long, List<EnvResourceVO>> baseResourceMap = new HashMap<>();
        Map<Long, List<EnvResourceVO>> optionResourceMap = new HashMap<>();
        this.getResourceMap(baseResourceMap, optionResourceMap, idSet);

        Comparator<EnvVO> comparator = new PropertyComparator<>(StringUtils.isEmpty(sort) ? FIELD_BIZ_NAME : sort, false, true);
        return envDTOList.stream().map(t -> {
            EnvVO env = EnvVO.from(t);
            String envTypeName = DictManager.parseAttrValue(CommonConstants.AttrSpecCode.DEPLOY_ENV_TYPE.name(), t.getEnvType());
            if (StringUtils.isEmpty(envTypeName)) {
                envTypeName = t.getEnvType();
            }
            env.setEnvTypeName(envTypeName);
            //TODO：统计服务数量
            env.setServiceNum(0);

            List<EnvResourceVO> baseResourceVOList = baseResourceMap.get(t.getId());
            List<EnvResourceVO> optionResourceVOList = optionResourceMap.get(t.getId());
            if(!CollectionUtils.isEmpty(baseResourceVOList)){
                env.setBaseEnvResource(baseResourceVOList);
                env.setBaseEnvResourceCount(baseResourceVOList.size());
            }
            if(!CollectionUtils.isEmpty(optionResourceVOList)){
                env.setOptionEnvResource(optionResourceVOList);
                env.setOptionEnvResourceCount(optionResourceVOList.size());
            }

            return env;
        }).sorted(ORDER_DESC.equalsIgnoreCase(order) ? comparator.reversed() : comparator).collect(Collectors.toList());
    }

    @Override
    public OperateReturnDTO deleteEnvById(Long id, String staff) {
        //TODO:校验已部署环境

        envResourceService.deleteResourceByEnvId(id);
        this.removeById(id);
        return new OperateReturnDTO(Boolean.TRUE);
    }

    @Override
    public EnvVO detailEnvById(Long id) {
        Env env = this.getById(id);
        EnvVO envVO = new EnvVO();
        BeanUtils.copyProperties(env,envVO);
        if (envVO != null) {
            String envTypeName = DictManager.parseAttrValue(CommonConstants.AttrSpecCode.DEPLOY_ENV_TYPE.name(), env.getEnvType());
            if(null == envTypeName){
                envTypeName = env.getEnvType();
            }
            envVO.setEnvTypeName(envTypeName);
        }
        return envVO;
    }

    private void getResourceMap(Map<Long, List<EnvResourceVO>> baseResourceMap, Map<Long, List<EnvResourceVO>> optionResourceMap, List<Long> idList) {
        List<EnvResource> envResourcesList = envResourceService.getEnvResourceByEnvIds(idList);

        if (CollectionUtils.isEmpty(envResourcesList)){
            return;
        }

        List<EnvResourceVO> envResourceVOList = new ArrayList<>();

        for(EnvResource envResource : envResourcesList){

            if(ResourceConstants.MainCategory.BASE.getValue().equals(envResource.getMainCategory())){
                envResourceVOList = baseResourceMap.get(envResource.getEnvId());
                if(null == envResourceVOList){
                    envResourceVOList = new ArrayList<>();
                    baseResourceMap.put(envResource.getEnvId(),envResourceVOList);
                }
            }
            if(ResourceConstants.MainCategory.OPTION.getValue().equals(envResource.getMainCategory())){
                envResourceVOList = optionResourceMap.get(envResource.getEnvId());
                if(null == envResourceVOList){
                    envResourceVOList = new ArrayList<>();
                    optionResourceMap.put(envResource.getEnvId(),envResourceVOList);
                }
            }

            EnvResourceVO envResourceVO = new EnvResourceVO();
            BeanUtils.copyProperties(envResource, envResourceVO);
            envResourceVO.setStatusCdName(DictManager.parseAttrValue(ResourceConstants.RESOURCESTATE,String.valueOf(envResourceVO.getStatusCd())));
            envResourceVOList.add(envResourceVO);
        }

    }

    private void updateEnvInfo(Long id, EnvVO envVo,String userName) {
        boolean dockerDeploy = false;
        boolean hostDeploy = false;


        if (!org.springframework.util.CollectionUtils.isEmpty(envVo.getBaseEnvResource())){
            for(EnvResourceVO envResourceVO : envVo.getBaseEnvResource()){
                if(ResourceConstants.SubCategory.DCOS.getValue().equals(envResourceVO.getSubCategory())){
                    dockerDeploy = Boolean.TRUE;
                }
                if(ResourceConstants.SubCategory.ECS.getValue().equals(envResourceVO.getSubCategory())){
                    hostDeploy = Boolean.TRUE;
                }
            }
        }

        LambdaUpdateWrapper<Env> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Env::getId, id)
                .set(Env::getEnvName,envVo.getEnvName())
                .set(Env::getEnvType,envVo.getEnvType())
                .set(Env::getVpcId,envVo.getVpcId())
                .set(Env::getVpcName,envVo.getVpcName())
                .set(Env::getUpdateTime,new Date())
                .set(Env::getUpdateBy,userName)
                .set(Env::getDockerDeploy,dockerDeploy)
                .set(Env::getHostDeploy,hostDeploy);

        this.update(updateWrapper);
    }



    private int checkBaseEnvResource(Long envId, List<EnvResourceVO> baseEnvResource) {
        // K8S 和 ECS 数量
        int k8sCount = 0;
        int ecsCount = 0;

        if(CollectionUtils.isEmpty(baseEnvResource)){
            throw BASE_RESOURCE_NOT_NULL.exception();
        }

        for(EnvResourceVO envResourceVO : baseEnvResource){
            if(ResourceConstants.SubCategory.DCOS.getValue().equals(envResourceVO.getSubCategory())){
                EnvResource envResource = envResourceService.getEnvResourceByInstIdAndNameSpace(envId,envResourceVO.getInstId(),envResourceVO.getNamespace());
                if(null != envResource){
                    throw K8S_RESOURCE_EXIST.exception();
                }
                k8sCount++;
            }
            if(ResourceConstants.SubCategory.ECS.getValue().equals(envResourceVO.getSubCategory())){
                ecsCount++;
            }
        }
        if (ecsCount < 1 && k8sCount != 1){
            throw BASE_RESOURCE_NOT_NULL.exception();
        }
        return k8sCount;
    }

    private void saveEnvResource(EnvVO envVO, Env env, String userName) {
        List<EnvResourceVO> baseEnvResource = envVO.getBaseEnvResource();
        List<EnvResourceVO> optionEnvResource = envVO.getOptionEnvResource();
        if (CollectionUtils.isEmpty(optionEnvResource)){
            baseEnvResource.addAll(optionEnvResource);
        }
        for(EnvResourceVO envResourceVO : baseEnvResource){
            envResourceVO.setEnvId(env.getId());
            envResourceVO.setResourcePoolCode(env.getResPoolCode());
            envResourceVO.setTenantId(env.getTenantId());
            envResourceVO.setCreateBy(userName);
            envResourceVO.setUpdateBy(userName);
            envResourceVO.setCreateTime(new Date());
            envResourceVO.setUpdateTime(new Date());
            if(ResourceConstants.SubCategory.MSERCM.getValue().equals(envResourceVO.getSubCategory())){
                //创建命名空间
                try {

//                    Response<TopologyInstInfoDTO> spuInst = orderproxyGatewayFeignClient.getSpuInst(envResourceVO.getInstId(),true);
//                    log.error("nacos信息：{}", JsonUtils.objectToJson(spuInst,""));
//                    if(spuInst.isSuccess()){
//                        TopologyInstInfoDTO data = spuInst.getData();
//                        List<InstAttrsDTO> paasInstAttrs = data.getPaasInstAttrs();
//                        if (!org.springframework.util.CollectionUtils.isEmpty(paasInstAttrs)){
//                            for(InstAttrsDTO instAttrsDTO : paasInstAttrs){
//                                if (MSERCM_SERVER_URL_KEY.equals(instAttrsDTO.getAttrKey())){
//                                    envResourceVO.setIp(instAttrsDTO.getAttrVal());
//                                }
//                            }
//                        }
//                    }
//                    envResourceService.createNameSpace(envResourceVO.getInstId(), env);
//
//                    iInstAkskService.createAndAddPerm(envResourceVO.getInstId(), env);
                }catch (Exception e){
                    log.error("创建NACOS命名空间失败，error:{}",e.getMessage().toString());
                }
            }

            if(ResourceConstants.SubCategory.DCOS.getValue().equals(envResourceVO.getSubCategory())){
                try {
//                    /**
//                     * 保存服务网格实例信息
//                     */
//                    istioInstInfoService.saveIstioInstInfo(envResourceVO.getResourceName(),envResourceVO.getInstId());
//                    //创建资源池cr的命名空间
//                    CrRel crRel = crRelService.getByResPoolCode(env.getResPoolCode());
//                    String crNamespace = TenantUtils.getCrNamespace(userInfo.getTenantCode(), userInfo.getTenantId());
//                    Boolean crNamespaceExist = imageRepositoryManager.crMsapNamespaceExist(crRel.getInstId(), crRel.getResPoolName()+"-"+crNamespace);
//                    if (!crNamespaceExist) {
//                        imageRepositoryManager.createCrMsapNamespace(crRel.getInstId(), crRel.getResPoolName()+"-"+crNamespace);
//                    }
//
//                    /**
//                     * 创建密钥
//                     */
//                    imageRepositoryManager.createImageSecret(crRel.getInstId(),envResourceVO.getResourceName(),envResourceVO.getNamespace(),crRel.getResPoolName()+"-"+crNamespace);
//                    /**
//                     * 安装webhook插件
//                     */
//                    K8sPluginInstanceDTO k8sPluginInstanceDTO = new K8sPluginInstanceDTO();
//                    ResultDTO resultDTO = ccseGatewayFeignClient.pluginInstance(envResourceVO.getResourceName(), k8sPluginInstanceDTO);
//                    log.error("插件安装返回：{}", l.objectToJson(resultDTO, ""));
                } catch (Exception e){
                    log.error("创建Harbor项目名称失败，error:{}", e.getMessage());
                }
            }

        }
        envResourceService.insertBatch(baseEnvResource);
    }

    private Env saveEnvInfo(EnvVO envVO, String userName, Long tenantId) {
        Env env = new Env();
        BeanUtils.copyProperties(envVO,env);
        env.setEnvUuid(UuidUtils.generateUuid());

        env.setResPoolName(envVO.getResPoolName());
        UserInfo userInfo = SecurityUtils.getUserInfo();
        env.setTenantId(userInfo.getTenantId());
        env.setCreateBy(userName);
        env.setCreateTime(new Date());
        env.setUpdateBy(userName);
        env.setUpdateTime(new Date());
        env.setTenantId(tenantId);
        if(!CollectionUtils.isEmpty(envVO.getBaseEnvResource())){
            for(EnvResourceVO envResourceVO : envVO.getBaseEnvResource()){
                if(ResourceConstants.SubCategory.DCOS.getValue().equals(envResourceVO.getSubCategory())){
                    env.setDockerDeploy(Boolean.TRUE);
                }
                if(ResourceConstants.SubCategory.ECS.getValue().equals(envResourceVO.getSubCategory())){
                    env.setHostDeploy(Boolean.TRUE);
                }
            }
        }
        envDao.insertEnv(env);

        return env;
    }

    private Env selectByEnvCode(String envCode,Long tenantId) {
        LambdaQueryWrapper<Env> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Env::getEnvCode, envCode).eq(Env::getTenantId, tenantId);
        List<Env> envList = this.list(queryWrapper);
        return CollectionUtils.isEmpty(envList)?null:envList.get(0);
    }

}
