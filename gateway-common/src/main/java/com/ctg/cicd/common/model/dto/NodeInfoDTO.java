package com.ctg.cicd.common.model.dto;

import lombok.Data;

/**
 * @author hel
 * @date 2023/5/18
 */
@Data
public class NodeInfoDTO {

    private Long id;

    private String nodeName;

    private String nodeType;

    private Long parentId;

    private String nodeLabel;

    private String description;

    private Boolean parentRoleExtends;

    private String nodePath;

    private Long tenantId;









}
