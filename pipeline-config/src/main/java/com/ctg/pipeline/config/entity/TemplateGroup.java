package com.ctg.pipeline.config.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author hel
 * @since 2022-11-23
 */
@Data
@TableName("p_ctg_template_group")
public class TemplateGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;


}
