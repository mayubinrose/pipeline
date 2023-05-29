package com.ctg.pipeline.execute.exceptions;

/**
 * @author zhiHuang
 * @date 2022/11/14 22:35
 **/
public class InvalidRequestException extends ExecuteException {

    public InvalidRequestException() {}

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRequestException(Throwable cause) {
        super(cause);
    }

    public InvalidRequestException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}