package com.ctg.cicd.config.entity.base;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("cicd_attr_spec")
public class AttrSpec {

    private Long id;

    private Long parentId;

    private String code;

    private String name;

    private String type;

    private Integer sequence;

    private String description;

    private String statusCd;

    private Date statusTime;

    private Date createTime;

    private String createStaff;

    private Date updateTime;

    private String updateStaff;
}