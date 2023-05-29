package com.ctg.pipeline.common.exception;


import lombok.Getter;

public enum BusinessException {


    APPLICATION_EXP_NOT_FOUND(20001, "应用[%s]不存在"),

    PIPEILINE_EXP_NOT_FOUND(30001, "流水线[%s]不存在"),
    PIPEILINE_EXP_APPLICATION_NOT_MATCH(30002, "流水线[%s]所在应用不正确"),
    PIPEILINE_EXP_ALREADY_EXIST(30001, "流水线[%s]已经存在"),


    STAGE_TEMPLATE_EXP_NOT_FOUND(40001, "模板[%s]不存在");

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
