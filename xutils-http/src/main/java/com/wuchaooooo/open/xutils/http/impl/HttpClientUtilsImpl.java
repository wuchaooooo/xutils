package com.wuchaooooo.open.xutils.http.impl;

import com.wuchaooooo.open.xutils.http.HttpClientUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HttpClientUtilsImpl implements HttpClientUtils {

    //默认Http client
    private static HttpClient client;

    private static RequestConfig requestConfig;

    //默认连接超时时间(ms)
    private static final Integer DEFAULT_CONNECTION_TIMEOUT = 100000;

    //默认请求连接超时时间
    private static final Integer DEFAULT_CONNECTION_REQUEST_TIMEOUT = 100000;

    //默认读取超时时间(ms)
    private static final Integer DEFAULT_SOCKET_TIMEOUT = 200000;

    //默认字符集
    private static final String DEFAULT_CHART_SET = "UTF-8";

    //构建默认的http client (支持http和https请求)
    static {
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());

            //同时支持http和https请求
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.
                    <ConnectionSocketFactory> create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslsf)
                    .build();

            //初始化连接管理器
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            //最大连接数，
            //TODO 将连接数写在配置文件读取
            cm.setMaxTotal(128);
            //最大路由
            cm.setDefaultMaxPerRoute(128);

            //默认requestConfig
            requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(DEFAULT_CONNECTION_REQUEST_TIMEOUT)
                    .setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT)
                    .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
                    .build();

            client = HttpClients.custom()
                    .setConnectionManager(cm)
                    .setDefaultRequestConfig(requestConfig)
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String get(String url, Map<String, String> query, Map<String, String> header) throws IOException {
        HttpClient client = null;
        HttpGet get = null;
        HttpResponse response = null;

        try {
            //请求参数encode
            if (!MapUtils.isEmpty(query)) {
                List<NameValuePair> nameValuePairList = Collections.EMPTY_LIST;
                for (Map.Entry<String, String> entry : query.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    NameValuePair nameValuePair = new BasicNameValuePair(key, value);
                    nameValuePairList.add(nameValuePair);
                }
                String encodeQuery = URLEncodedUtils.format(nameValuePairList, "UTF-8");
                url = new StringBuffer(url).append("?").append(encodeQuery).toString();
            }

            get = new HttpGet(url);

            //设置HTTP Request参数
            get.setConfig(requestConfig);

            //请求头
            if (!MapUtils.isEmpty(header)) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    get.setHeader(entry.getKey(), entry.getValue());
                }
            }

            //执行请求
            if (url.startsWith("https")) {
                client = createSSLInsecureClient();
                response = client.execute(get);
            } else {
                //执行http请求
                client = HttpClientUtilsImpl.client;
                response = client.execute(get);
            }

            String responseBody = IOUtils.toString(response.getEntity().getContent(), DEFAULT_CHART_SET);
            if (!StringUtils.isEmpty(responseBody)) {
                //TODO
                //打印相关日志
            }
            return responseBody;
        } catch (GeneralSecurityException e) {
            throw new IOException();
        } finally {
            get.releaseConnection();
            if (url.startsWith("https") && client instanceof CloseableHttpClient) {
                ((CloseableHttpClient) client).close();
            }
        }

    }

    @Override
    public String get(String url, Map<String, String> query) throws IOException {
        return get(url, query, null);
    }

    @Override
    public String get(String url) throws IOException {
        return get(url, null, null);
    }

    @Override
    public String post(String url, String jsonBody, Map<String, String> header) throws IOException {
        HttpClient client = null;
        HttpPost post = null;
        HttpResponse response = null;

        try {
            post = new HttpPost(url);

            //设置HTTP Request参数
            post.setConfig(requestConfig);

            //请求头
            if (!MapUtils.isEmpty(header)) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    post.setHeader(entry.getKey(), entry.getValue());
                }
            }

            //请求体
            if (StringUtils.isNotBlank(jsonBody)) {
                StringEntity stringEntity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
                post.setEntity(stringEntity);
            }

            //执行请求
            if (url.startsWith("https")) {
                client = createSSLInsecureClient();
                response = client.execute(post);
            } else {
                //执行http请求
                client = HttpClientUtilsImpl.client;
                response = client.execute(post);
            }

            String responseBody = IOUtils.toString(response.getEntity().getContent(), DEFAULT_CHART_SET);
            if (!StringUtils.isEmpty(responseBody)) {
                //TODO
                //打印相关日志
            }
            return responseBody;
        } catch (GeneralSecurityException e) {
            throw new IOException();
        } finally {
            post.releaseConnection();
            if (url.startsWith("https") && client instanceof CloseableHttpClient) {
                ((CloseableHttpClient) client).close();
            }
        }
    }

    @Override
    public String post(String url, String jsonBody) throws IOException {
        return post(url, jsonBody, null);
    }

    @Override
    public String put(String url, String jsonBody, Map<String, String> header) throws IOException {
        HttpClient client = null;
        HttpPut put = null;
        HttpResponse response = null;

        try {
            put = new HttpPut(url);

            //设置HTTP Request参数
            put.setConfig(requestConfig);

            //请求头
            if (!MapUtils.isEmpty(header)) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    put.setHeader(entry.getKey(), entry.getValue());
                }
            }

            //请求体
            if (StringUtils.isNotBlank(jsonBody)) {
                StringEntity stringEntity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
                put.setEntity(stringEntity);
            }

            //执行请求
            if (url.startsWith("https")) {
                client = createSSLInsecureClient();
                response = client.execute(put);
            } else {
                //执行http请求
                client = HttpClientUtilsImpl.client;
                response = client.execute(put);
            }

            String responseBody = IOUtils.toString(response.getEntity().getContent(), DEFAULT_CHART_SET);
            if (!StringUtils.isEmpty(responseBody)) {
                //TODO
                //打印相关日志
            }
            return responseBody;
        } catch (GeneralSecurityException e) {
            throw new IOException();
        } finally {
            put.releaseConnection();
            EntityUtils.consume(response.getEntity());
            if (url.startsWith("https") && client instanceof CloseableHttpClient) {
                ((CloseableHttpClient) client).close();
            }
        }
    }

    @Override
    public String put(String url, String jsonBody) throws IOException {
        return put(url, jsonBody, null);
    }

    @Override
    public String delete(String url, Map<String, String> header) throws IOException {
        HttpClient client = null;
        HttpDelete delete = null;
        HttpResponse response = null;

        try {
            delete = new HttpDelete(url);

            //设置HTTP Request参数
            delete.setConfig(requestConfig);

            //请求头
            if (!MapUtils.isEmpty(header)) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    delete.setHeader(entry.getKey(), entry.getValue());
                }
            }

            //执行请求
            if (url.startsWith("https")) {
                client = createSSLInsecureClient();
                response = client.execute(delete);
            } else {
                //执行http请求
                client = HttpClientUtilsImpl.client;
                response = client.execute(delete);
            }

            String responseBody = IOUtils.toString(response.getEntity().getContent(), DEFAULT_CHART_SET);
            if (!StringUtils.isEmpty(responseBody)) {
                //TODO
                //打印相关日志
            }
            return responseBody;
        } catch (GeneralSecurityException e) {
            throw new IOException();
        } finally {
            delete.releaseConnection();
            if (url.startsWith("https") && client instanceof CloseableHttpClient) {
                ((CloseableHttpClient) client).close();
            }
        }
    }

    @Override
    public String delete(String url) throws IOException {
        return delete(url, null);
    }

    public static CloseableHttpClient createSSLInsecureClient() throws GeneralSecurityException {
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        }).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }
}
