package me.boot.web.mvc.advice;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @description
 * @date 2023/08/06
 **/
@Slf4j
//@ControllerAdvice
public class LogResponseBodyAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType mediaType,
        Class converterType, ServerHttpRequest request, ServerHttpResponse response) {
        Method method = returnType.getMethod();
        log.info("{}.{}: {}", method.getDeclaringClass().getSimpleName(), method.getName(), body);
        return body;
    }
}
