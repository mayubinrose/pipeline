package com.ctg.pipeline.execute.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS;

/**
 * @author zhiHuang
 * @date 2022/11/14 20:53
 **/
public class ExecuteObjectMapper {
    private ExecuteObjectMapper() {}

    private static final ObjectMapper INSTANCE = newInstance();

    public static ObjectMapper newInstance() {
        ObjectMapper instance = new ObjectMapper();
        instance.registerModule(new Jdk8Module());
        instance.registerModule(new JavaTimeModule());
        instance.disable(READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
        instance.disable(WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);
        instance.disable(FAIL_ON_UNKNOWN_PROPERTIES);
        instance.setSerializationInclusion(NON_NULL);
        return instance;
    }

    public static ObjectMapper getInstance() {
        return INSTANCE;
    }


}