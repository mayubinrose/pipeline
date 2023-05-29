package com.ctg.cicd.gate.handler;

import com.ctg.cicd.common.base.ResponseVO;
import com.ctg.cicd.common.exception.CodedException;
import com.ctg.cicd.common.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

@Slf4j
@Order(-1)
@RestControllerAdvice(
        basePackages = {
                "com.ctg.cicd.gate"
        }
)
public class GlobalExceptionHandler {

    public GlobalExceptionHandler() {

    }

    @ExceptionHandler({CodedException.class})
    public Object onCodedException(CodedException e, HttpServletRequest request, HttpServletResponse response) {
        log.error("Request: [{}] failed!", request.getRequestURI(), e);
        return ResponseVO.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler({NumberFormatException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object onCodedException(NumberFormatException e, HttpServletRequest request, HttpServletResponse response) {
        log.error("Request: [{}] failed!", request.getRequestURI(), e);
        return ResponseVO.fail(HttpStatus.BAD_REQUEST.value(), "Illegal argument: " + e.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object onCodedException(IllegalArgumentException e, HttpServletRequest request, HttpServletResponse response) {
        log.error("Request: [{}] failed!", request.getRequestURI(), e);
        return ResponseVO.fail(HttpStatus.BAD_REQUEST.value(), "Illegal argument: " + e.getMessage());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object onConstraintViolationException(ConstraintViolationException e, HttpServletRequest request, HttpServletResponse response) {
        log.error("Internal server error, request: [{}]!", request.getRequestURI(), e);

        Set<ConstraintViolation<?>> constraintViolationSet = e.getConstraintViolations();
        if (CollectionUtils.isNotEmpty(constraintViolationSet)) {
            StringBuffer sbf = new StringBuffer();
            constraintViolationSet.forEach(item -> {
                sbf.append(item.getMessageTemplate() + "；");
            });
            return ResponseVO.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), sbf.toString().substring(0, sbf.length() - 1));
        } else {
            return ResponseVO.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object onCodedException(MethodArgumentNotValidException e, HttpServletRequest request, HttpServletResponse response) {
        log.error("Request: [{}] failed!", request.getRequestURI(), e);
        String errorMessage = this.getErrorMessage(e);
        return ResponseVO.fail(HttpStatus.BAD_REQUEST.value(), errorMessage);
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object onException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        log.error("Internal server error, request: [{}]!", request.getRequestURI(), e);
        return ResponseVO.SERVER_ERROR;
    }

    private String findCauseMessage(Exception e) {
        Throwable temp = e;
        while (temp.getCause() != null) {
            temp = temp.getCause();
        }
        return temp.getMessage();
    }

    private String getErrorMessage(MethodArgumentNotValidException e) {
        StringBuilder stringBuilder = new StringBuilder("Illegal argument: ");
        int i = 0;
        List<ObjectError> errorList = e.getBindingResult().getAllErrors();
        for (ObjectError error : errorList) {
            stringBuilder.append(error.getDefaultMessage());
            if (++i < errorList.size()) {
                stringBuilder.append(", ");
            } else {
                stringBuilder.append(". ");
            }
        }
        return stringBuilder.toString();
    }


}
