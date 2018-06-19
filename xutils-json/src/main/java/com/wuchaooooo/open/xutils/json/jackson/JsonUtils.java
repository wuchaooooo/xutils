package com.wuchaooooo.open.xutils.json.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * json工具类
 * 转换失败 则统一返回 null
 */
public class JsonUtils {

    private static ObjectMapper defaultObjectMapper = new ObjectMapper();

    /**
     * json对象字符串 转成 java对象
     * @param jsonObjectStr json对象字符串
     * @param clazz java对象类
     * @param <T> 范型类
     * @return 反序列化后的对象
     */
    public static <T> T json2Obj(String jsonObjectStr, Class<T> clazz) {
        return json2Obj(jsonObjectStr, clazz, defaultObjectMapper);
    }

    /**
     * json对象字符串 转成 java对象
     * @param jsonObjectStr json对象字符串
     * @param clazz java对象类
     * @param objectMapper json转换工具类
     * @param <T> 范型类
     * @return 反序列化后的对象
     */
    public static <T> T json2Obj(String jsonObjectStr, Class<T> clazz, ObjectMapper objectMapper) {
        T result = null;
        try {
            result = objectMapper.readValue(jsonObjectStr, clazz);
        } catch (IOException e) {
            //TODO
        }
        return result;
    }

    /**
     * json数组字符串 转成 java list集合
     * @param jsonArrayStr json数组字符串
     * @param clazz java对象类
     * @param <T> 范型类
     * @return 反序列化后的list集合
     */
    public static <T> List<T> json2List(String jsonArrayStr, Class<T> clazz) {
        List<T> result = Collections.EMPTY_LIST;
        try {
            result = defaultObjectMapper.readValue(jsonArrayStr, defaultObjectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            //TODO
        }
        return result;
    }

    /**
     * json数组字符串 转成 java list集合
     * @param jsonArrayStr json数组字符串
     * @param clazz java对象类
     * @param objectMapper json转换工具类
     * @param <T> 范型类
     * @return 反序列化后的list集合
     */

    public static <T> List<T> json2List(String jsonArrayStr, Class<T> clazz, ObjectMapper objectMapper) {
        List<T> result = Collections.EMPTY_LIST;
        try {
            result = objectMapper.readValue(jsonArrayStr, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            //TODO
        }
        return result;
    }

    /**
     * json对象字符串 转成 java Map
     * @param jsonObjectStr json对象字符串
     * @return Map
     */
    public static Map<?, ?> json2Map(String jsonObjectStr) {
        return json2Obj(jsonObjectStr, Map.class);
    }

    /**
     * java对象 转成 json对象/数组 字符串
     * @param obj 待序列化的java对象
     * @return json对象/数组 字符串
     */
    public static String obj2Json(Object obj) {
        return obj2Json(obj, defaultObjectMapper);
    }

    /**
     * java对象 转成 json对象/数组 字符串
     * @param obj 待序列化的java对象
     * @param objectMapper json转换工具类
     * @return json对象/数组 字符串
     */
    public static String obj2Json(Object obj, ObjectMapper objectMapper) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
