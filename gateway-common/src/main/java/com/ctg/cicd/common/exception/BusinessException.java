package com.ctg.cicd.common.exception;


import lombok.Getter;

public enum BusinessException {


    NODE_INFO_NOT_FOUND(20001, "节点[%s]不存在或已被删除"),
    ROOT_NODE_DELETE_NOT_ALLOW(20002, "根节点不允许删除"),
    EXIST_CHILD_NODE_DELETE_NOT_ALLOW(20003, "当前节点存在子节点不允许删除"),
    EXIST_PIPELINE_DELETE_NOT_ALLOW(20004, "当前节点存在流水线不允许删除"),
    ROOT_ADD_NODE_TYPE_ERROR(20005, "根节点只能添加项目节点"),
    PROJECT_ADD_NODE_TYPE_ERROR(20006, "项目节点只允许添加服务节点"),
    SERVICE_ADD_NODE_TYPE_ERROR(20007, "服务节点不允许添加子节点"),


    EXIST_ROLE_NAME_NOT_ALLOW(30001, "当前根节点下角色名已存在"),

    NOT_EXIST_ROLE_DELETE_NOT_ALLOW(30002, "不存在角色不允许删除"),

    ROLE_UPDATE_FAIL(30003, "角色更新失败"),

    NOT_EXIST_CURRENT_ROLE(30004,"当前角色不存在"),

    EXIST_ROLE_FUNCITON_ADD_NOT_ALLOW(30005,"当前角色的功能已经存在，不能重复绑定")
    ;


    @Getter
    private int code;

    @Getter
    private String msg;

    BusinessException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CodedException exception() {
        return new CodedException(this.code, this.msg);
    }

    public CodedException exception(Object... args) {
        String msg = String.format(this.msg, args);
        return new CodedException(this.code, msg);
    }

}
