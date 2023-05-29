package com.ctg.cicd.config.dao;

import com.ctg.cicd.common.model.vo.NodeUserRoleVO;
import com.ctg.cicd.config.entity.NodeUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hel
 * @since 2023-05-17
 */
@Repository
public interface NodeUserRoleDao extends BaseMapper<NodeUserRole> {


    List<NodeUserRole> listNodeUserRole(@Param("nodeId") Long nodeId, @Param("userId") Long userId, @Param(
            "userName") String userName, @Param("nodeName") String nodeName);

    List<Long> getDistinctUserByNodeId(@Param("nodeId") Long nodeId);
}
