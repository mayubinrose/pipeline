package com.ctg.cicd.common.model.bo;

import lombok.Data;

import java.util.List;

/**
 * @author jirt
 * @date 2020/5/12 10:23
 */
@Data
public class AttrSpecBO implements Comparable<AttrSpecBO> {

    private Long id;

    private String code;

    private String name;

    private String type;

    private Integer sequence;

    private String description;

    private List<AttrValueBO> attrValueList;

    private List<AttrSpecBO> children;

    @Override
    public int compareTo(AttrSpecBO object) {
        return this.sequence - object.sequence;
    }
}
