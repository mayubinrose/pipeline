package com.ctg.pipeline.execute.exceptions;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author zhiHuang
 * @date 2022/11/14 22:58
 **/
public class ValidationException extends InvalidRequestException
        implements HasAdditionalAttributes{
    private final Collection errors;

    public ValidationException(Collection errors) {
        this.errors = errors;
    }

    public ValidationException(String message, Collection errors) {
        super(message);
        this.errors = errors;
    }

    public ValidationException(String message, Throwable cause, Collection errors) {
        super(message, cause);
        this.errors = errors;
    }

    public ValidationException(Throwable cause, Collection errors) {
        super(cause);
        this.errors = errors;
    }

    public ValidationException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace,
            Collection errors) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errors = errors;
    }

    @Override
    public Map<String, Object> getAdditionalAttributes() {
        return errors != null && !errors.isEmpty()
                ? Collections.singletonMap("errors", errors)
                : Collections.emptyMap();
    }

}