package com.ctg.cicd.common.model.bo;

import lombok.Data;

/**
 * @author jirt
 * @date 2020/5/12 10:19
 */
@Data
public class AttrSpecValueBO {

    private Long attrSpecId;

    private Long parentId;

    private String attrSpecCode;

    private String attrSpecName;

    private String attrSpecType;

    private Integer attrSpecSequence;

    private String attrSpecDescription;

    private Long attrValueId;

    private String attrValue;

    private String attrValueName;

    private Integer attrValueSequence;

    private String attrValueDescription;
}
