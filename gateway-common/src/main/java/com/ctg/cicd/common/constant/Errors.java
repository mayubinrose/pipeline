package com.ctg.cicd.common.constant;

/**
* 业务异常编码枚举类
* <p>用于定义业务异常编码和说明，建议所有模块都定义在一个枚举类中，以模块名为前缀区分</p>
*/
public enum Errors {
    USER_NOT_FOUND(2404, "User not found"),
    USER_DISABLED(2405, "User disabled"),
    USER_EXPIRED(2406, "User expired");

    private int code;
    private String msg;

    Errors(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

//    public CodedException exception() {
//        return new CodedException(this.code, this.msg);
//    }
//
//    public CodedException exception(Object... args) {
//        return new CodedException(this.code, String.format(this.msg, args));
//    }
}