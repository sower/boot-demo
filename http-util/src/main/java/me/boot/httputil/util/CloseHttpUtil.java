package me.boot.httputil.util;

import com.alibaba.fastjson.JSON;
import java.net.URI;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * CloseableHttpClient
 *
 * @date 2023/03/26
 **/
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class CloseHttpUtil implements HttpsUtil {

    private static CloseableHttpClient client;

    @Autowired
    public void setClient(CloseableHttpClient client) {
        this.client = client;
    }

    public Map<String, String> queryParams;
    private String url;

    private Map<String, String> headers;
    private Object body;


    @SneakyThrows
    public URI requestUrl(String url, Map<String, String> queryParams) {
        URIBuilder uriBuilder = new URIBuilder(url);
        if (ObjectUtils.isNotEmpty(queryParams)) {
            queryParams.forEach((key, value) -> {
                if (value != null) {
                    uriBuilder.addParameter(key, value);
                }
            });
        }
        return uriBuilder.build();
    }

    // Create a custom response handler
    final HttpClientResponseHandler<String> responseHandler = response -> {
        final int status = response.getCode();
        log.info("<=== Received response code is {} , response headers: {}", status,
            response.getHeaders());
        HttpEntity entity = response.getEntity();
        String responseEntity = entity != null ? EntityUtils.toString(entity) : null;
        log.info("response body:{}", responseEntity);
        return responseEntity;

//    if (status >= HttpStatus.SC_SUCCESS && status < HttpStatus.SC_REDIRECTION) {
//      throw new ClientProtocolException("Unexpected response status: " + status);
//    }
    };

    @Override
    @SneakyThrows
    public String executeRequest(String method) {
        URI requestUrl = requestUrl(url, queryParams);
        log.info("===> Sending {} request: {}", method, requestUrl);
        log.info("request headers: {}", headers);
        HttpUriRequestBase request = new HttpUriRequestBase(method, requestUrl);

        if (ObjectUtils.isNotEmpty(headers)) {
            headers.forEach(request::addHeader);
        }

        if (body != null) {
            log.info("request body: {}", body);
            request.setEntity(
                new StringEntity(JSON.toJSONString(body), ContentType.APPLICATION_JSON));
        }

        return client.execute(request, responseHandler);
    }

}
