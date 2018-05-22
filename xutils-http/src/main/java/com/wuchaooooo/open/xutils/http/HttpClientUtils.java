package com.wuchaooooo.open.xutils.http;

import lombok.NonNull;

import java.io.IOException;
import java.util.Map;

public interface HttpClientUtils {

    /**
     * <p>Http GET请求</p>
     * @param url 请求路径
     * @param query 请求参数
     * @param header 请求头
     * @return Http响应体
     * @throws IOException get请求失败
     * @author wuchaooooo
     * @since 2018/5/21 11:16
     */
    String get(@NonNull String url, Map<String, String> query, Map<String, String> header) throws IOException;

    /**
     * <p>Http GET请求</p>
     * @param url 请求路径
     * @param query 请求参数
     * @return Http响应体
     * @throws IOException get请求失败
     * @author wuchaooooo
     * @since 2018/5/21 11:16
     */
    String get(@NonNull String url, Map<String, String> query) throws IOException;

    /**
     * <p>Http GET请求</p>
     * @param url 请求路径
     * @return Http响应体
     * @throws IOException get请求失败
     * @author wuchaooooo
     * @since 2018/5/21 11:16
     */
    String get(@NonNull String url) throws IOException;

    /**
     * <p>Http POST请求</p>
     * @param url 请求路径
     * @param jsonBody 请求体(json格式)
     * @param header 请求头
     * @return Http响应体
     * @throws IOException post请求失败
     * @author wuchaooooo
     * @since 2018/5/21 11:16
     */
    String post(@NonNull String url, @NonNull String jsonBody, Map<String, String> header) throws IOException;

    /**
     * <p>Http POST请求</p>
     * @param url 请求路径
     * @param jsonBody 请求体(json格式)
     * @return Http响应体
     * @throws IOException post请求失败
     * @author wuchaooooo
     * @since 2018/5/21 11:16
     */
    String post(@NonNull String url, @NonNull String jsonBody) throws IOException;

    /**
     * <p>Http PUT请求</p>
     * @param url 请求路径
     * @param jsonBody 请求体(json格式)
     * @param header 请求头
     * @return Http响应体
     * @throws IOException put请求失败
     * @author wuchaooooo
     * @since 2018/5/21 11:16
     */
    String put(@NonNull String url, @NonNull String jsonBody, Map<String, String> header) throws IOException;

    /**
     * <p>Http PUT请求</p>
     * @param url 请求路径
     * @param jsonBody 请求体(json格式)
     * @return Http响应体
     * @throws IOException put请求失败
     * @author wuchaooooo
     * @since 2018/5/21 11:16
     */
    String put(@NonNull String url, @NonNull String jsonBody) throws IOException;

    /**
     * <p>Http DELETE请求</p>
     * @param url 请求路径
     * @return Http响应体
     * @throws IOException delete请求失败
     * @author wuchaooooo
     * @since 2018/5/21 11:16
     */
    String delete(@NonNull String url, Map<String, String> header) throws IOException;

    /**
     * <p>Http DELETE请求</p>
     * @param url 请求路径
     * @return Http响应体
     * @throws IOException delete请求失败
     * @author wuchaooooo
     * @since 2018/5/21 11:16
     */
    String delete(@NonNull String url) throws IOException;
}
