package me.boot.httputil.interceptor;

import feign.FeignException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.RetryableException;
import feign.Retryer;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.spring.SpringFormEncoder;
import java.util.Date;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import me.boot.base.constant.Constants;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

/**
 * @description
 * @date 2023/07/19
 **/
@Slf4j
public class TraceInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(Constants.TRACE_ID, UUID.randomUUID().toString());
    }

    /**
     * 自定义重试机制
     */
    @Bean
    public Retryer feignRetryer() {
        //最大请求次数为5，初始间隔时间为100ms，下次间隔时间1.5倍递增，重试间最大间隔时间为1s，
        return new Retryer.Default();
    }

    @Bean
    public ErrorDecoder feignError() {
        return (key, response) -> {
            FeignException exception = FeignException.errorStatus(key, response);
            log.error("request error ({}): {}", exception.status(), exception.contentUTF8());
            if (response.status() == HttpStatus.TOO_MANY_REQUESTS.value()) {
                log.warn("Too many requests, ready retry");
                throw new RetryableException(response.status(), exception.getMessage(),
                    response.request().httpMethod(), exception, new Date(), response.request());
            }

//            return new ErrorDecoder.Default().decode(key, response);
            return exception;
        };
    }

    /**
     *  form-url-encoded 编码器
     */
    @Bean
    public Encoder formEncoder(ObjectFactory<HttpMessageConverters> converters) {
        return new SpringFormEncoder(new SpringEncoder(converters));
    }


}
