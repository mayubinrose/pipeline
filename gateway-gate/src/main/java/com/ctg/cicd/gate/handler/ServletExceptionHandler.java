package com.ctg.cicd.gate.handler;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.ctg.cloud.paascommon.vo.Response;
import com.ctg.eadp.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jirt
 * @date 2020/4/30 11:13
 */
@Slf4j
@ControllerAdvice
@ConditionalOnWebApplication
public class ServletExceptionHandler {

    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            HttpMediaTypeNotAcceptableException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    @ResponseBody
    public Response<String> handleServletException(Exception e) {
        log.error(e.getMessage(), e);
        if (e instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException mse = (MissingServletRequestParameterException) e;
            HashMap<String, String> resp = MapUtil.newHashMap(2);
            resp.put("field", mse.getParameterName());
            resp.put("message", "此属性是必填项");
            return Response.fail(500, JSONUtil.toJsonStr(resp));
        }
        int code = -1;
        return Response.fail(code, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Response methodArgumentNotValidException(MethodArgumentNotValidException ex){
        if (ex.getBindingResult().hasErrors()) {
            List<Map<String, String>> list = new ArrayList<>();
            for (ObjectError objectError : ex.getBindingResult().getAllErrors()) {
                Map<String, String> map = new HashMap<>();
                if (objectError instanceof FieldError) {
                    FieldError fieldError = (FieldError) objectError;
                    map.put("field", fieldError.getField());
                    map.put("message", fieldError.getDefaultMessage());
                } else {
                    map.put("field", objectError.getObjectName());
                    map.put("message", objectError.getDefaultMessage());
                }
                list.add(map);
            }
            return Response.fail(500, JsonUtils.toString(list));
        }else {
            return Response.fail(500, ex.getMessage());
        }
    }
}
