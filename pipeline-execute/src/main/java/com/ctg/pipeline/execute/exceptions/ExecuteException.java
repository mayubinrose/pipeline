package com.ctg.pipeline.execute.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.annotation.Nullable;

/**
 * @author zhiHuang
 * @date 2022/11/14 21:00
 **/
@Getter
public class ExecuteException extends RuntimeException implements HasAdditionalAttributes{

    @Nullable
    private String userMessage;

    /**
     * Whether or not the exception is explicitly known to be retryable.
     *
     * <p>If the result is NULL, the exception's retry characteristics are undefined and thus retries
     * on the original logic that caused the exception may have undefined behavior.
     */
    @Setter
    @Nullable
    @Accessors(chain = true)
    private Boolean retryable;

    public ExecuteException() {}

    public ExecuteException(String message) {
        super(message);
    }

    public ExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExecuteException(Throwable cause) {
        super(cause);
    }

    public ExecuteException(String message, String userMessage) {
        super(message);
        this.userMessage = userMessage;
    }

    public ExecuteException(String message, Throwable cause, String userMessage) {
        super(message, cause);
        this.userMessage = userMessage;
    }

    public ExecuteException(Throwable cause, String userMessage) {
        super(cause);
        this.userMessage = userMessage;
    }

    public ExecuteException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}