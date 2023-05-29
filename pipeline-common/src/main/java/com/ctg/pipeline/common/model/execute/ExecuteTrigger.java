package com.ctg.pipeline.common.model.execute;

import java.util.List;
import java.util.Map;

/**
 * 触发运行参数
 * @author zhiHuang
 * @date 2022/11/14 14:44
 **/
public class ExecuteTrigger {

    // 请求数据demo：{"type":"manual","parameters":{"branch":"master","giturl":"11111"},"user":"[anonymous]"}

    private String user;

    private String type;

    private Map parameters;

    private String parentPipelineId;

    private List<Map> artifcats;

    private  List<Map> expectedArtifacts;

    private List<Map> resolvedExpectedArtifacts;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map getParameters() {
        return parameters;
    }

    public void setParameters(Map parameters) {
        this.parameters = parameters;
    }

    public String getParentPipelineId() {
        return parentPipelineId;
    }

    public void setParentPipelineId(String parentPipelineId) {
        this.parentPipelineId = parentPipelineId;
    }

    public List<Map> getArtifcats() {
        return artifcats;
    }

    public void setArtifcats(List<Map> artifcats) {
        this.artifcats = artifcats;
    }

    public List<Map> getExpectedArtifacts() {
        return expectedArtifacts;
    }

    public void setExpectedArtifacts(List<Map> expectedArtifacts) {
        this.expectedArtifacts = expectedArtifacts;
    }

    public List<Map> getResolvedExpectedArtifacts() {
        return resolvedExpectedArtifacts;
    }

    public void setResolvedExpectedArtifacts(List<Map> resolvedExpectedArtifacts) {
        this.resolvedExpectedArtifacts = resolvedExpectedArtifacts;
    }
}