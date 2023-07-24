package me.boot.httputil.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import java.util.UUID;
import me.boot.base.constant.Constants;

/**
 * @description
 * @date 2023/07/19
 **/
public class TraceInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(Constants.TRACE_ID, UUID.randomUUID().toString());
    }
}
