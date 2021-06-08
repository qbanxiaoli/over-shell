package com.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;


/**
 * @author qbanxiaoli
 * @description Json字符串工具类
 * @create 2018/7/3 13:31
 */
@Slf4j
public class JsonUtil {

    @SneakyThrows
    public static String toJsonString(Object object) {
        // 使用ObjectMapper来转化对象为Json
        ObjectMapper objectMapper = new ObjectMapper();
        // 配置objectMapper忽略空属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper.writeValueAsString(object);
    }

    @SneakyThrows
    public static <T> T praseObject(String json, Class<T> componentClass) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, componentClass);
    }

}