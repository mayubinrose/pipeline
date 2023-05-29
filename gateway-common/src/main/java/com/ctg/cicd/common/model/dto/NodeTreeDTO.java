package com.ctg.cicd.common.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @author hel
 * @date 2023/5/25
 */
@Data
public class NodeTreeDTO {
    private Long id;

    private Long parentId;

    private String nodeName;

    private String nodeType;

    private List<NodeTreeDTO> children;
}
