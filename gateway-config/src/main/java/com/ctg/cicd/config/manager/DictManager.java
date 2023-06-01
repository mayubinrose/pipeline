package com.ctg.cicd.config.manager;

import com.ctg.cicd.common.model.bo.AttrSpecBO;
import com.ctg.cicd.common.model.bo.AttrSpecValueBO;
import com.ctg.cicd.common.model.bo.AttrValueBO;
import com.ctg.cicd.common.util.CollectionUtils;
import com.ctg.cicd.config.dao.AttrSpecDao;
import com.ctg.cicd.config.entity.base.AttrSpec;
import com.ctg.eadp.common.constant.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author jirt
 */
@Slf4j
@Component("dictManager")
public class DictManager implements InitializingBean {

    private static final int INITIAL_CAPACITY = 100;

    private static Map<String, AttrSpecBO> attrValueMap = new HashMap<>(INITIAL_CAPACITY);

    private static Map<String, AttrSpecBO> attrSpecMap = new HashMap<>(INITIAL_CAPACITY);

    @Autowired
    private AttrSpecDao attrSpecDao;

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void refresh() {
        log.debug("Start refreshing dictionary. ");
        try {
            attrValueMap = this.getAttrValueMap();
            attrSpecMap = this.getAttrSpecMap();
        } catch (Exception e) {
            log.error("Refreshing dictionary failure. ", e);
        }
        log.debug("Finish refreshing dictionary. ");
    }

    public static String parseAttrValue(String attrSpecCode, String attrValue) {
        String attrValueName = null;
        AttrSpecBO attrSpec = attrValueMap.get(attrSpecCode);
        if (null != attrSpec && CollectionUtils.isNotEmpty(attrSpec.getAttrValueList())) {
            for (AttrValueBO attrValueBO : attrSpec.getAttrValueList()) {
                if (attrValueBO.getAttrValue().equals(attrValue)) {
                    attrValueName = attrValueBO.getName();
                    break;
                }
            }
        }
        return attrValueName;
    }

    public static AttrSpecBO getWithAttrValue(String attrSpecCode) {
        return attrValueMap.get(attrSpecCode);
    }

    public static AttrSpecBO getWithChildren(String attrSpecCode) {
        return attrSpecMap.get(attrSpecCode);
    }

    private void init() {
        initAttrValueMap();
        initAttrSpecMap();
    }

    private void initAttrSpecMap() {
        log.debug("Start initializing attrSpec map. ");
        try {
            attrSpecMap = this.getAttrSpecMap();
        } catch (Exception e) {
            log.error("Initializing attrSpec map failure! ", e);
            System.exit(-1);
        }
        log.debug("Finish initializing attrSpec map. ");
    }

    private Map<String, AttrSpecBO> getAttrSpecMap() {
        Map<String, AttrSpecBO> result = new HashMap<>(INITIAL_CAPACITY);
        List<AttrSpec> attrSpecList = attrSpecDao.list(CommonConstants.StatusCd.S0A.name());
        if (CollectionUtils.isEmpty(attrSpecList)) {
            log.debug("Finish initializing, attrSpec map is empty. ");
            return result;
        }
        Map<Long, List<AttrSpecBO>> map = new HashMap<>();
        for (AttrSpec attrSpec : attrSpecList) {
            if (null == attrSpec.getParentId()) {
                continue;
            }
            List<AttrSpecBO> children = map.computeIfAbsent(attrSpec.getParentId(), k -> new ArrayList<>());
            AttrSpecBO attrSpecBO = this.constructAttrSpec(attrSpec);
            children.add(attrSpecBO);
        }
        if (CollectionUtils.isEmpty(map)) {
            log.debug("Finish initializing, attrSpec map is empty. ");
            return result;
        }
        for (AttrSpec attrSpec : attrSpecList) {
            if (null != attrSpec.getParentId()) {
                continue;
            }
            List<AttrSpecBO> children = map.get(attrSpec.getId());
            if (CollectionUtils.isEmpty(children)) {
                continue;
            }
            Collections.sort(children);
            AttrSpecBO attrSpecBO = this.constructAttrSpec(attrSpec);
            attrSpecBO.setChildren(children);
            result.put(attrSpec.getCode(), attrSpecBO);
            log.debug("Put attrSpec [{}] into attrSpec map. ", attrSpec.getCode());
        }
        return result;
    }

    private void initAttrValueMap() {
        log.debug("Start initializing attrValue map. ");
        try {
            attrValueMap = this.getAttrValueMap();
        } catch (Exception e) {
            log.error("Initializing attrValue map failure! ", e);
            System.exit(-1);
        }
        log.debug("Finish initializing attrValue map. ");
    }

    private Map<String, AttrSpecBO> getAttrValueMap() {
        Map<String, AttrSpecBO> result = new HashMap<>(INITIAL_CAPACITY);
        List<AttrSpecValueBO> attrSpecValueList = attrSpecDao.listWithValue(CommonConstants.StatusCd.S0A.name());
        if (CollectionUtils.isNotEmpty(attrSpecValueList)) {
            for (AttrSpecValueBO attrSpecValue : attrSpecValueList) {
                AttrSpecBO attrSpec = result.get(attrSpecValue.getAttrSpecCode());
                if (null == attrSpec) {
                    attrSpec = this.constructAttrSpec(attrSpecValue);
                    result.put(attrSpecValue.getAttrSpecCode(), attrSpec);
                    log.debug("Put attrSpec [{}] into attrValue map. ", attrSpecValue.getAttrSpecCode());
                }
                AttrValueBO attrValue = this.constructAttrValue(attrSpecValue);
                attrSpec.getAttrValueList().add(attrValue);
            }
        }
        return result;
    }

    private AttrValueBO constructAttrValue(AttrSpecValueBO attrSpecValue) {
        AttrValueBO attrValue = new AttrValueBO();
        attrValue.setAttrValue(attrSpecValue.getAttrValue());
        attrValue.setName(attrSpecValue.getAttrValueName());
        attrValue.setDescription(attrSpecValue.getAttrValueDescription());
        attrValue.setSequence(attrSpecValue.getAttrValueSequence());
        return attrValue;
    }

    private AttrSpecBO constructAttrSpec(AttrSpec attrSpec) {
        AttrSpecBO attrSpecBO = new AttrSpecBO();
        attrSpecBO.setId(attrSpec.getId());
        attrSpecBO.setCode(attrSpec.getCode());
        attrSpecBO.setName(attrSpec.getName());
        attrSpecBO.setType(attrSpec.getType());
        attrSpecBO.setDescription(attrSpec.getDescription());
        attrSpecBO.setSequence(attrSpec.getSequence());
        return attrSpecBO;
    }

    private AttrSpecBO constructAttrSpec(AttrSpecValueBO attrSpecValue) {
        AttrSpecBO attrSpec = new AttrSpecBO();
        attrSpec.setId(attrSpecValue.getAttrSpecId());
        attrSpec.setCode(attrSpecValue.getAttrSpecCode());
        attrSpec.setName(attrSpecValue.getAttrSpecName());
        attrSpec.setType(attrSpecValue.getAttrSpecType());
        attrSpec.setDescription(attrSpecValue.getAttrSpecDescription());
        List<AttrValueBO> attrValueList = new ArrayList<>();
        attrSpec.setAttrValueList(attrValueList);
        return attrSpec;
    }

}
