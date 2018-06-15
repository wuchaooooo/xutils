package com.wuchaooooo.open.xutils.json.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    public static ObjectMapper defaultObjectMapper = new ObjectMapper();

    /**
     * json对象字符串 转成 java对象
     * @param jsonObjectStr json对象字符串
     * @param clazz java对象类
     * @param <T> 范型类
     * @return 反序列化后的对象
     */
    public static <T> T json2Object(String jsonObjectStr, Class<T> clazz) {
        return json2Object(jsonObjectStr, clazz, defaultObjectMapper);
    }

    public static <T> T json2Object(String jsonObjectStr, Class<T> clazz, ObjectMapper objectMapper) {
        T resut = null;
        try {
            resut = objectMapper.readValue(jsonObjectStr, clazz);
        } catch (IOException e) {
            //TODO
        }
        return resut;
    }

    public static <T> List<T> json2List(String jsonArrayStr, Class<T> clazz) {
        List<T> result = Collections.EMPTY_LIST;
        try {
            result = defaultObjectMapper.readValue(jsonArrayStr, defaultObjectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            //TODO
        }
        return result;
    }

    public static <T> List<T> json2List(String jsonArrayStr, Class<T> clazz, ObjectMapper objectMapper) {
        List<T> result = Collections.EMPTY_LIST;
        try {
            result = objectMapper.readValue(jsonArrayStr, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            //TODO
        }
        return result;
    }

    public static Map<?, ?> json2Map(String jsonObjectStr) {
        return json2Object(jsonObjectStr, Map.class);
    }
}
