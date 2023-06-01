package com.ctg.cicd.config.entity.base;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("cicd_attr_value")
public class AttrValue {

    private Long id;

    private Long attrSpecId;

    private String attrValue;

    private String name;

    private Integer sequence;

    private String description;

    private String statusCd;

    private Date statusTime;

    private Date createTime;

    private String createStaff;

    private Date updateTime;

    private String updateStaff;
}