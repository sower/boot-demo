package me.boot.web.advice;

import com.alibaba.fastjson2.JSON;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

/**
 * @description
 * @date 2023/08/06
 **/
@Slf4j
//@ControllerAdvice
public class LogRequestBodyAdvice implements RequestBodyAdvice {

    @Override
    public boolean supports(MethodParameter parameter, Type targetType,
        Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter,
        Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage,
        MethodParameter parameter, Type targetType,
        Class<? extends HttpMessageConverter<?>> converterType) {
        return logMethod(body, parameter);
    }

    private static Object logMethod(Object body, MethodParameter parameter) {
        Method method = parameter.getMethod();
        log.info("{}.{}: {}", method.getDeclaringClass().getSimpleName(), method.getName(),
            JSON.toJSONString(body));
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage,
        MethodParameter parameter, Type targetType,
        Class<? extends HttpMessageConverter<?>> converterType) {
        return logMethod(body, parameter);
    }
}