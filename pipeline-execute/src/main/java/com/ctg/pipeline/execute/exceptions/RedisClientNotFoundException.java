package com.ctg.pipeline.execute.exceptions;

/**
 * @author zhiHuang
 * @date 2022/11/15 11:06
 **/
public class RedisClientNotFoundException extends RuntimeException{
    public RedisClientNotFoundException(String message) {
        super(message);
    }

}