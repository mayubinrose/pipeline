package com.ctg.cicd.common.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EnvDTO {
	private Long id;

	private String envUuid;

	private Long tenantId;

	private String envName;

	private String envCode;

	private String envType;

	private Long resPoolId;

	private Long vpcId;

	private String vpcName;

	private String resPoolName;

	private String resPoolCode;

	private Long projectId;

	private String projectCode;

	private String projectName;

	private String instId;

	private String resourceName;

	private Long resourcePoolId;

	private String resourcePoolCode;

	private String namespaceInstId;

	private String namespace;

	private Date createTime;

	private String createStaff;

}
