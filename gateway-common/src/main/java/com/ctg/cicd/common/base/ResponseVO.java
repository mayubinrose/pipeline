package com.ctg.cicd.common.base;

import com.ctg.cloud.paascommon.vo.Response;
import lombok.Data;

/**
 * Api接口规范返回体
 *
 * @author huangZhi
 * @date 2023/05/19 11:05
 **/

@Data
public class ResponseVO<T> {

    private int statusCode = CODE_SUCCESS;
    private String message;
    private T returnObj;

    private int code; //异常错误码，与前端无关

    public static final int CODE_SUCCESS = 2000;
    public static final ResponseVO FOREVER_REDIRECT = new ResponseVO(301, "永久重定向");
    public static final ResponseVO TEMPLETE_REDIRECT = new ResponseVO(302, "临时重定向");
    public static final ResponseVO REQUEST_PARAM_ERROR = new ResponseVO(400, "请求参数错误");

    public static final ResponseVO NO_AUTH_ERROR = new ResponseVO(403, "权限不足");
    public static final ResponseVO NO_EXIST_ERROR = new ResponseVO(404, "请求路径不存在");
    public static final ResponseVO MANY_REQUEST_ERROR = new ResponseVO(429, "请求次数过多");
    public static final ResponseVO SERVER_ERROR = new ResponseVO(500, "服务端异常");
    public static final ResponseVO GATEWAY_ERROR = new ResponseVO(502, "网关异常");
    public static final ResponseVO GATEWAY_UNAVAILABLE_ERROR = new ResponseVO(503, "服务不可用");
    public static final ResponseVO GATEWAY_TIMEOUT_ERROR = new ResponseVO(504, "网关超时");

    private ResponseVO() {
    }

    public ResponseVO(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    private ResponseVO(T returnObj) {
        this.returnObj = returnObj;
    }

    public static ResponseVO success() {
        return new ResponseVO();
    }

    public static <T> ResponseVO<T> success(T returnObj) {
        return new ResponseVO(returnObj);
    }

    public static ResponseVO fail(int statusCode, String message) {
        ResponseVO<Object> r = new ResponseVO();
        r.setStatusCode(statusCode);
        r.setMessage(message);
        return r;
    }


}