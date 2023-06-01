package com.ctg.cicd.config.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctg.cicd.common.constant.ResourceConstants;
import com.ctg.cicd.common.model.bo.AttrSpecValueBO;
import com.ctg.cicd.common.model.dto.*;
import com.ctg.cicd.common.model.vo.EnvResourceVO;
import com.ctg.cicd.common.util.CollectionUtils;
import com.ctg.cicd.config.dao.AttrSpecDao;
import com.ctg.cicd.config.dao.EnvResourceDao;
import com.ctg.cicd.config.entity.Env;
import com.ctg.cicd.config.entity.EnvResource;
import com.ctg.cicd.config.feign.ICloudManagementClient;
import com.ctg.cicd.config.feign.IDesktopGatewayFeignClient;
import com.ctg.cicd.config.feign.IOrderproxyGatewayFeignClient;
import com.ctg.cicd.config.manager.DictManager;
import com.ctg.cicd.config.service.ICCSECallService;
import com.ctg.cicd.config.service.IEnvResourceService;
import com.ctg.cloud.paascommon.utils.SecurityUtils;
import com.ctg.cloud.paascommon.vo.Response;
import com.ctg.eadp.common.util.JsonUtils;
import com.ctgudal.eadp.dto.ClusterV2DTO;
import com.ctgudal.eadp.dto.ResultDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ctg.eadp.common.exception.BusinessException.*;

/**
 * <p>
 * 环境资源关联表 服务实现类
 * </p>
 *
 * @author hel
 * @since 2023-05-30
 */
@Slf4j
@Service
public class EnvResourceService extends ServiceImpl<EnvResourceDao, EnvResource> implements IEnvResourceService {

    private static final String PRODUCT_KEY = "map";
    private static final String VPCID_KEY = "1010";
    private static final String OPTIONRESOURCE = "OPTIONRESOURCE";

    @Autowired
    private EnvResourceDao envResourceDao;

    @Autowired
    private IDesktopGatewayFeignClient desktopGatewayFeignClient;

    @Autowired
    private ICloudManagementClient cloudManagementFeignClient;

    @Autowired
    private IOrderproxyGatewayFeignClient orderproxyGatewayFeignClient;

    @Autowired
    private AttrSpecDao attrSpecDao;

    @Autowired
    private ICCSECallService ccseCallService;

    @Override
    public void insertBatch(List<EnvResourceVO> baseEnvResourceList) {
        if(CollectionUtils.isEmpty(baseEnvResourceList)){
            return;
        }
        List<EnvResource> entityList = new ArrayList<>(baseEnvResourceList.size());
        for(EnvResourceVO envResourceVO:baseEnvResourceList){
            EnvResource entity = new EnvResource();
            BeanUtils.copyProperties(envResourceVO,entity);
            entityList.add(entity);
        }
        saveBatch(entityList);
    }

    @Override
    public EnvResource getEnvResourceByInstIdAndNameSpace(Long envId, String instId, String namespace) {
        EnvResource envResource = envResourceDao.getEnvResourceByInstIdAndNameSpace(envId, instId, namespace);
        return envResource;
    }

    @Override
    public void deleteResourceByEnvId(Long envId) {
        LambdaQueryWrapper<EnvResource> removeWrapper = Wrappers.lambdaQuery();
        removeWrapper.in(EnvResource::getEnvId, envId);
        this.remove(removeWrapper);
    }

    @Override
    public List<VpcInfoDTO> listVpc(String resPoolId) {
        Response<PageInfo<VpcInfoDTO>> pageInfoResponse = desktopGatewayFeignClient.pageVpc(resPoolId, 1, 1, 100);
        if (pageInfoResponse.isSuccess()){
            return pageInfoResponse.getData().getList();
        }
        return null;
    }

    @Override
    public List<EnvResourceDTO> listBaseResource(Long tenantId, Long userId, String resPoolCode, String subCategory, Long vpcId, Long nodeId)  {

        List<EnvResourceDTO> resourceDTOList = new ArrayList<>();

        if(ResourceConstants.SubCategory.ECS.getValue().equals(subCategory)){
            log.info("ECS资源查询, tenantId={}, userId={}, resPoolId={}",tenantId, userId, resPoolCode);

            Response<PageInfo<TopologyHostDTO>> response = cloudManagementFeignClient.getTopologyHosts(null, null,
                    null, null, null, tenantId, userId, resPoolCode, 1, 10000);
            log.info("ECS资源查询,response={}. ", JsonUtils.toString(response));

            if (!response.isSuccess()) {
                throw ENV_HOSTS_INFO_FAILURE.exception();
            }
            PageInfo<TopologyHostDTO> data = response.getData();
            List<TopologyHostDTO> list = data.getList();
            if(com.ctg.eadp.common.util.CollectionUtils.isNotEmpty(list)){
                for(TopologyHostDTO topologyHostDTO : list){
                    if(ResourceConstants.ECS_RUN_STATUS.equals(topologyHostDTO.getStatus())){
                        EnvResourceDTO envResourceDTO = new EnvResourceDTO();
                        envResourceDTO.setInstId(topologyHostDTO.getHostCode());
                        envResourceDTO.setResourceName(topologyHostDTO.getHostName());
                        envResourceDTO.setIp(topologyHostDTO.getHostIp());
                        envResourceDTO.setMainCategory(ResourceConstants.MainCategory.BASE.getValue());
                        envResourceDTO.setOrderTime(topologyHostDTO.getOrderTime());
                        envResourceDTO.setSpuName("弹性云主机");
                        envResourceDTO.setSpec(topologyHostDTO.getSpecCatName());
                        envResourceDTO.setStatusCd(String.valueOf(topologyHostDTO.getStatus()));
                        envResourceDTO.setStatusCdName(DictManager.parseAttrValue(ResourceConstants.RESOURCESTATE,String.valueOf(topologyHostDTO.getStatus())));
                        envResourceDTO.setSubCategory("ecs");
                        resourceDTOList.add(envResourceDTO);
                    }
                }
            }
        }else {
            String spuId = DictManager.parseAttrValue(subCategory,subCategory);
            if (StringUtils.isBlank(spuId)){
                return resourceDTOList;
            }
            log.error("环境资源查询-----tenantId={},userId={},resPoolId={},spuId={},parSpuInstId={}",
                    tenantId,userId,resPoolCode,spuId,null);

            Response<PageInfo<TopologyInstInfoDTO>> response = orderproxyGatewayFeignClient.spuInst(tenantId, userId,
                    resPoolCode, Arrays.asList(spuId.split(",")), null, Arrays.asList(ResourceConstants.RUN_STATUS, ResourceConstants.CHANGING_STATUS),1, 100000);
            log.error("环境资源查询-----response={}",JsonUtils.objectToJson(response,"1"));

            if (!response.isSuccess()){
                throw BASE_RESOURCE_INFO_FAILURE.exception();
            }

            PageInfo<TopologyInstInfoDTO> data = response.getData();
            List<TopologyInstInfoDTO> list = data.getList();
            if(com.ctg.eadp.common.util.CollectionUtils.isNotEmpty(list)){


                for(TopologyInstInfoDTO topologyInstInfoDTO : list){
                    if (!(ResourceConstants.RUN_STATUS == topologyInstInfoDTO.getBizState() || ResourceConstants.CHANGING_STATUS == topologyInstInfoDTO.getBizState())) {
                        continue;
                    }

                    /**
                     * 没传vpcId说明不需要根据vpcId进行筛选
                     */
                    if (null != vpcId){
                        HashMap<String, Object> productProperty = topologyInstInfoDTO.getProductProperty();
                        if (null == productProperty){
                            continue;
                        }
                        Object objectMap = productProperty.get(PRODUCT_KEY);
                        if (null == objectMap){
                            continue;
                        }
                        HashMap<String,Object> map = (HashMap<String,Object>)objectMap;
                        Object vpcMap =  map.get(VPCID_KEY);
                        if (null == vpcMap){
                            continue;
                        }
                        String productVpcId = (String)vpcMap;
                        if (!String.valueOf(vpcId).equals(productVpcId)){
                            continue;
                        }
                    }

                    EnvResourceDTO envResourceDTO = new EnvResourceDTO();
                    envResourceDTO.setInstId(topologyInstInfoDTO.getSpuInstId());
                    envResourceDTO.setResourceName(topologyInstInfoDTO.getSpuInstName());
                    envResourceDTO.setMainCategory(ResourceConstants.MainCategory.BASE.getValue());
                    envResourceDTO.setOrderTime(new Date(Long.valueOf(topologyInstInfoDTO.getCreatedTime())));
                    envResourceDTO.setSpuName(topologyInstInfoDTO.getSpuName());
                    envResourceDTO.setSpec(topologyInstInfoDTO.getSkuName());
                    envResourceDTO.setStatusCd(String.valueOf(topologyInstInfoDTO.getBizState()));
                    envResourceDTO.setStatusCdName(DictManager.parseAttrValue(ResourceConstants.RESOURCESTATE,String.valueOf(topologyInstInfoDTO.getBizState())));
                    String subCat = ResourceConstants.SUBCATEGORY_MAP.get(topologyInstInfoDTO.getAppCode().toLowerCase());
                    envResourceDTO.setSubCategory(StringUtils.isBlank(subCat) ? topologyInstInfoDTO.getAppCode().toLowerCase() : subCat);
                    envResourceDTO.setVersion(topologyInstInfoDTO.getInnerVersionNo());
                    resourceDTOList.add(envResourceDTO);
                }
            }
        }
        return resourceDTOList;
    }

    @Override
    public List<EnvResourceDTO> listOptionResource(Long tenantId, Long userId, String resPoolCode, Long vpcId) {

        log.info("环境资源查询-----tenantId={},userId={},resPoolId={}",
                tenantId,userId,resPoolCode);
        Response<PageInfo<TopologyInstInfoDTO>> response = orderproxyGatewayFeignClient.spuInst(tenantId, userId,
                resPoolCode, null,null, null, 1, 10000);
        log.info("环境资源查询-----response={}",JsonUtils.objectToJson(response,"1"));

        if (!response.isSuccess()){
            throw OPTION_RESOURCE_INFO_FAILURE.exception();
        }

        PageInfo<TopologyInstInfoDTO> data = response.getData();
        List<TopologyInstInfoDTO> list = data.getList();
        List<EnvResourceDTO> resourceDTOList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(list)){
            AttrSpecValueBO attrSpecValueBO = attrSpecDao.selectValueByCode(OPTIONRESOURCE);

            for(TopologyInstInfoDTO topologyInstInfoDTO : list){
                if(!attrSpecValueBO.getAttrValue().contains(topologyInstInfoDTO.getAppCode().toLowerCase())
                        ||!(ResourceConstants.RUN_STATUS==topologyInstInfoDTO.getBizState())){
                    continue;
                }
                /**
                 * 没传vpcId说明不需要根据vpcId进行筛选
                 */
                if (null != vpcId){
                    HashMap<String, Object> productProperty = topologyInstInfoDTO.getProductProperty();
                    if (null == productProperty){
                        continue;
                    }
                    Object objectMap = productProperty.get(PRODUCT_KEY);
                    if (null == objectMap){
                        continue;
                    }
                    HashMap<String,Object> map = (HashMap<String,Object>)objectMap;
                    Object vpcMap =  map.get(VPCID_KEY);
                    if (null == vpcMap){
                        continue;
                    }
                    String productVpcId = (String)vpcMap;
                    if (!String.valueOf(vpcId).equals(productVpcId)){
                        continue;
                    }
                }
                EnvResourceDTO envResourceDTO = new EnvResourceDTO();
                envResourceDTO.setInstId(topologyInstInfoDTO.getSpuInstId());
                envResourceDTO.setResourceName(topologyInstInfoDTO.getSpuInstName());
                envResourceDTO.setMainCategory(ResourceConstants.MainCategory.OPTION.getValue());
                envResourceDTO.setOrderTime(new Date(Long.valueOf(topologyInstInfoDTO.getCreatedTime())));
                envResourceDTO.setSpuName(topologyInstInfoDTO.getSpuName());
                envResourceDTO.setSpec(topologyInstInfoDTO.getSkuName());
                envResourceDTO.setStatusCd(String.valueOf(topologyInstInfoDTO.getBizState()));
                envResourceDTO.setStatusCdName(DictManager.parseAttrValue(ResourceConstants.RESOURCESTATE,String.valueOf(topologyInstInfoDTO.getBizState())));
                envResourceDTO.setSubCategory(topologyInstInfoDTO.getAppCode().toLowerCase());
                envResourceDTO.setVersion(topologyInstInfoDTO.getInnerVersionNo());
                resourceDTOList.add(envResourceDTO);
            }
        }

        return resourceDTOList;
    }

    @Override
    public List<EnvResource> getEnvResourceByEnvIds(List<Long> idList) {

        if(CollectionUtils.isEmpty(idList)){
            return null;
        }
        LambdaQueryWrapper<EnvResource> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(EnvResource::getEnvId,idList);
        return list(queryWrapper);
    }

    @Override
    public PageInfo<EnvResourceDTO> pageQuery(Integer pageNum, Integer pageSize, Long tenantId, Long envId, String mainCategory, String subCategory) {
        PageHelper.startPage(pageNum, pageSize);
        List<EnvResourceDTO> list = envResourceDao.page(tenantId, envId, mainCategory, subCategory);
        PageInfo<EnvResourceDTO> result = new PageInfo<>(list);
        List<EnvResourceDTO> envResourceDTOList = result.getList();
        if(CollectionUtils.isEmpty(envResourceDTOList)){
            return result;
        }
        for(EnvResourceDTO envResourceDTO : envResourceDTOList){
            //TODO:set istio info
//            IstioInstInfo instInfo = istioInstInfoService.getByK8sInstId(envResourceDTO.getInstId());
//            if (null != instInfo){
//                envResourceDTO.setOpenIstio(true);
//            }else {
//                envResourceDTO.setOpenIstio(false);
//            }
            envResourceDTO.setStatusCdName(DictManager.parseAttrValue(ResourceConstants.RESOURCESTATE,String.valueOf(envResourceDTO.getStatusCd())));
        }
        return result;
    }

    @Override
    public String getAppUrl(Long envId, String instId, Boolean isNameSpace) {
        EnvResource envResource = envResourceDao.getEnvResourceByEnvIdAndInstId(envId, instId);
        if(ResourceConstants.SubCategory.ECS.getValue().equals(envResource.getSubCategory())){
            String ecsUrl = DictManager.parseAttrValue(ResourceConstants.SubCategory.ECS.getValue(),ResourceConstants.ECS_URL);
            String resourceName = envResource.getResourceName();
            String hostId = resourceName.split("_")[1].split("-")[0];
            return ecsUrl+"&topologyId="+envId+"&resPoolId="+envResource.getResourcePoolId()+"&regionid="+envResource.getResourcePoolCode()+"#/resource/host/ecs/detail/"+hostId+"/"+resourceName;
        }else {
            log.info("资源查询控制台地址查询-----subCategory={},resourcePoolCode={}", envResource.getSubCategory(),envResource.getResourcePoolCode());
            String appCode = envResource.getSubCategory();
            AttrSpecValueBO attrSpecValueBO = attrSpecDao.selectValueByCode(appCode);
            log.info("资源查询控制台地址查询-----response={}",JsonUtils.objectToJson(attrSpecValueBO,"1"));

            String baseUrl = attrSpecValueBO.getAttrValue();
            if(com.ctg.eadp.common.constant.ResourceConstants.SubCategory.DCOS.getValue().equals(envResource.getSubCategory())){
                ResultDTO<ClusterV2DTO> k8sClusterDetail = ccseCallService.getK8sClusterDetail(envResource.getResourceName());
                ClusterV2DTO clusterDTO = k8sClusterDetail.getData();
                if(isNameSpace){
                    return baseUrl+"#/namespace/ns_overview?clusterId="+clusterDTO.getId()+"&clusterName="+clusterDTO.getClusterName()
                            +"&namespaceName="+envResource.getNamespace();
                }else {
                    return baseUrl+"#/cluster/cluster-info?clusterId="+clusterDTO.getId()+"&clusterName="+clusterDTO.getClusterName();
                }
            }else if (com.ctg.eadp.common.constant.ResourceConstants.SubCategory.MSERCM.getValue().equals(envResource.getSubCategory())){
                return baseUrl+"#/instance/service-manage?instId="+envResource.getInstId()+"&instName="+envResource.getResourceName()
                        +"&spuType=nacos";
            }else if(com.ctg.eadp.common.constant.ResourceConstants.SubCategory.MSEGW.getValue().equals(envResource.getSubCategory())){
                return baseUrl+"#/gateway/basic?instId="+envResource.getInstId()+"&instName="+envResource.getResourceName();
            }else if(com.ctg.eadp.common.constant.ResourceConstants.SubCategory.REDIS.getValue().equals(envResource.getSubCategory())){
                return baseUrl+"#/user/detail?name="+envResource.getInstId()+"&type=3";
            }else if(com.ctg.eadp.common.constant.ResourceConstants.SubCategory.CTYUNDB.getValue().equals(envResource.getSubCategory())){
                return baseUrl+"#/index";
            }else if(com.ctg.eadp.common.constant.ResourceConstants.SubCategory.ROCKETMQ.getValue().equals(envResource.getSubCategory())){
                return baseUrl+"#/clusters/"+envResource.getInstId()+"/advance/nav/home";
            }else if(com.ctg.eadp.common.constant.ResourceConstants.SubCategory.KAFKA.getValue().equals(envResource.getSubCategory())){
                return baseUrl+"#/kafka/inst-info?spuInstId="+envResource.getInstId()+"&clusterName="+envResource.getResourceName();
            }

            return "";
        }
    }

    @Override
    public OperateReturnDTO syncResourceInfo(Long envId) {

        List<EnvResource> resourceList = getEnvResourceByEnvId(envId);
        if(CollectionUtils.isEmpty(resourceList)){
            return new OperateReturnDTO(Boolean.TRUE);
        }
        Long tenantId = SecurityUtils.getCurrentTenantId();
        Long userId = SecurityUtils.getUserId();
        String resourcePoolId = resourceList.get(0).getResourcePoolCode();

        log.info("环境资源查询-----tenantId={},userId={},resPoolId={}",
                tenantId,userId,resourcePoolId);
        Response<PageInfo<TopologyInstInfoDTO>> pageInfoResponse = orderproxyGatewayFeignClient.spuInst(tenantId,
                userId, resourcePoolId, null,null, null, 1, 10000);
        log.error("环境资源查询-----response={}",JsonUtils.objectToJson(pageInfoResponse,"1"));

        if(!pageInfoResponse.isSuccess()){
            throw DCOS_CONNECT_FAILURE.exception(pageInfoResponse.getMsg());
        }

        List<TopologyInstInfoDTO> list = pageInfoResponse.getData().getList();

        if(com.ctg.eadp.common.util.CollectionUtils.isEmpty(list)){
            return new OperateReturnDTO(Boolean.TRUE);
        }

        Map<String,TopologyInstInfoDTO> map = new HashMap<>();

        for(TopologyInstInfoDTO topologyInstInfoDTO : list){
            map.put(topologyInstInfoDTO.getSpuInstId(),topologyInstInfoDTO);
        }

        for(EnvResource resource : resourceList){
            TopologyInstInfoDTO topologyInstInfoDTO = map.get(resource.getInstId());
            if(null != topologyInstInfoDTO){
                LambdaUpdateWrapper<EnvResource> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(EnvResource::getId, resource.getId())
                        .set(EnvResource::getStatusCd,DictManager.parseAttrValue(ResourceConstants.RESOURCESTATE,String.valueOf(topologyInstInfoDTO.getBizState())));

                this.update(updateWrapper);
            }
        }

        return new OperateReturnDTO(Boolean.TRUE);
    }

    private List<EnvResource> getEnvResourceByEnvId(Long envId) {
        LambdaQueryWrapper<EnvResource> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(EnvResource::getEnvId, envId);
        return this.list(queryWrapper);
    }

}
