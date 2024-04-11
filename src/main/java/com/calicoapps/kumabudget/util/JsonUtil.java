package com.calicoapps.kumabudget.util;

import com.calicoapps.kumabudget.error.ErrorCode;
import com.calicoapps.kumabudget.error.KumaException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper;

    private static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); // Module mandatory to be able to serialize LocalTime. Added with jackson-datatype-jsr310
        }
        return objectMapper;
    }

    public static String toJson(Object obj) {
        try {
            return getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            log.error("toJson" , e);
            throw new KumaException(ErrorCode.INTERNAL_JSON);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return getObjectMapper().readValue(json, clazz);
        } catch (IOException e) {
            log.error("fromJson" , e);
            throw new KumaException(ErrorCode.INTERNAL_JSON);
        }
    }

    public static <T> List<T> fromJsonArray(String json, Class<T> clazz) {
        try {
            return getObjectMapper()
                    .readValue(json,
                            objectMapper
                                    .getTypeFactory()
                                    .constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            log.error("fromJsonArray" , e);
            throw new KumaException(ErrorCode.INTERNAL_JSON);
        }
    }

    public static <T> boolean areJsonEquals(String json1, String json2, Class<T> clazz) {
        T obj1 = fromJson(json1, clazz);
        T obj2 = fromJson(json2, clazz);
        return obj1.equals(obj2);
    }
}