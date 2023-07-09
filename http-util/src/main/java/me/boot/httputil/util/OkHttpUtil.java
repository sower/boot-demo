package me.boot.httputil.util;

import com.alibaba.fastjson.JSON;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Semaphore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Ok http
 *
 * @date 2022/09/04
 */
@Slf4j
@Component
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OkHttpUtil implements HttpsUtil {

    private static OkHttpClient client;

    @Autowired
    public void setClient(OkHttpClient client) {
        this.client = client;
    }

    public Map<String, String> queryParams;
    private String url;
    public static MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    private Request.Builder request;
    private Map<String, String> headers;
    private RequestBody body;

    public static RequestBody jsonBody(@NotNull String json) {
        return RequestBody.create(json, JSON_MEDIA_TYPE);
    }

    public static RequestBody jsonBody(Object obj) {
        if (obj == null) {
            return null;
        }
        return jsonBody(JSON.toJSONString(obj));
    }

    public static FormBody formBody(Map<String, String> map) {
        FormBody.Builder formBody = new FormBody.Builder(StandardCharsets.UTF_8);
        map.forEach(formBody::addEncoded);
        return formBody.build();
    }

    public OkHttpUtil fileBody() {
        RequestBody fileBody =
            RequestBody.create(new File("path/attachment.png"), MediaType.parse("image/png"));
        body =
            new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "head_img", fileBody)
                .addFormDataPart("key", "val")
                .build();
        return this;
    }

    private void beforeRequest() {
        log.info("build request");

        // 构建url
        HttpUrl.Builder urlBuilder = HttpUrl.get(url).newBuilder();
        if (ObjectUtils.isNotEmpty(queryParams)) {
            queryParams.forEach(
                (key, value) -> {
                    if (value != null) {
                        urlBuilder.addQueryParameter(key, value);
                    }
                });
        }

        request = new Request.Builder().url(urlBuilder.build());
        if (ObjectUtils.isNotEmpty(headers)) {
            request.headers(Headers.of(headers));
        }
    }

    @Override
    @SneakyThrows
    public String executeRequest(String method) {
        try {
            beforeRequest();
            request.method(method, body);
            Response response = client.newCall(request.build()).execute();
            ResponseBody responseBody = Optional.ofNullable(response.body())
                .orElse(ResponseBody.create("", null));
            return responseBody.string();
        } finally {
            log.info("end request");
        }
    }


    private static volatile Semaphore semaphore = null;

    /**
     * 用于异步请求时，控制访问线程数，返回结果
     */
    private static Semaphore getSemaphoreInstance() {
        // 只能1个线程同时访问
        synchronized (OkHttpUtil.class) {
            if (semaphore == null) {
                semaphore = new Semaphore(0);
            }
        }
        return semaphore;
    }

    public String async() {
        StringBuffer buffer = new StringBuffer();
        client
            .newCall(request.build())
            .enqueue(
                new Callback() {

                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        buffer.append("request failed: ").append(e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response)
                        throws IOException {
                        ResponseBody body = response.body();
                        if (body != null) {
                            buffer.append(body.string());
                        }
                        getSemaphoreInstance().release();
                    }
                });
        getSemaphoreInstance().release();
        return buffer.toString();
    }

}
