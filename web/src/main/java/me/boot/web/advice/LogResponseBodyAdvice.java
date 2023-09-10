package me.boot.web.advice;

import com.alibaba.fastjson2.JSON;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @description
 * @date 2023/08/06
 **/
@Slf4j
@ControllerAdvice
public class LogResponseBodyAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        Method method = methodParameter.getMethod();
        String url = serverHttpRequest.getURI().toASCIIString();
        log.info("{}.{}, url={}, result={}", method.getDeclaringClass().getSimpleName(), method.getName(), url, JSON.toJSONString(body));
        return body;
    }
}
