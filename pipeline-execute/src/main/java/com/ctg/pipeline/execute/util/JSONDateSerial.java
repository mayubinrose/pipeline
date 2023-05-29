package com.ctg.pipeline.execute.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class JSONDateSerial extends JsonSerializer<Long> {
    public JSONDateSerial() {
    }

    public void serialize(Long aLong, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        jsonGenerator.writeString(df.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(aLong), ZoneId.of("Asia/Shanghai"))));
    }
}