package com.ctg.cicd.config.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctg.cicd.common.model.bo.AttrSpecValueBO;
import com.ctg.cicd.config.entity.EnvResource;
import com.ctg.cicd.config.entity.base.AttrSpec;
import com.ctg.eadp.common.dto.portal.MenuModuleDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jirt
 * @date 2020/5/12 10:10
 */
@Repository
public interface AttrSpecDao extends BaseMapper<EnvResource> {

    /**
     * 获取带有属性值的属性规格列表
     * @param statusCd 状态
     * @return List<AttrSpecValueBO>
     */
    List<AttrSpecValueBO> listWithValue(@Param("statusCd") String statusCd);

    /**
     * 获取所有属性规格
     * @param statusCd 状态
     * @return List<AttrSpec>
     */
    List<AttrSpec> list(@Param("statusCd") String statusCd);


    AttrSpecValueBO selectValueByCode(String code);
}
