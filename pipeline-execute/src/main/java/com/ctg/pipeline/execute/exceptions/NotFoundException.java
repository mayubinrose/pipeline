package com.ctg.pipeline.execute.exceptions;

/**
 * @author zhiHuang
 * @date 2022/11/14 22:20
 **/
public class NotFoundException extends ExecuteException {
    public NotFoundException() {}

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    public NotFoundException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}