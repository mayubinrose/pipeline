package com.ctg.cicd.config.dao;

import com.ctg.cicd.common.model.dto.UserInfoDTO;
import com.ctg.cicd.config.entity.SettingsUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
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
public interface SettingsUserDao extends BaseMapper<SettingsUser> {

    List<UserInfoDTO> queryUserByExcludeUser(@Param("excludeUserIds") List<Long> excludeUserIds,@Param("nodeRootId") Long nodeRootId,@Param("keyword") String keyword);
}
