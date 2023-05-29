package com.ctg.cicd.config.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctg.cicd.common.model.dto.NodeInfoDTO;
import com.ctg.cicd.config.entity.NodeInfo;
import org.apache.ibatis.annotations.Param;
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
public interface NodeInfoDao extends BaseMapper<NodeInfo> {

    int insertNodeInfo(NodeInfo insert);

    List<NodeInfoDTO> selectChildList(@Param("parentId") Long parentId,@Param("nodeName") String nodeName);

    Long getNodeRootId(Long tenantId);
}
