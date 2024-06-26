package me.boot.httputil.config;

import feign.Feign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * FeignConfig
 *
 * @since 2023/01/15
 */
@Configuration
@EnableFeignClients(basePackages = "me.boot.httputil")
@ConditionalOnClass(Feign.class)
//@AutoConfigureBefore(FeignAutoConfiguration.class)
public class FeignConfig {

    @Bean
    public WebMvcRegistrations feignWebRegistrations() {
        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new FeignRequestMappingHandlerMapping();
            }
        };
    }

    /**
     * 过滤同时被 @FeignClient和@RequestMapping 修饰的类
     */
    private static class FeignRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

        @Override
        protected boolean isHandler(@NonNull Class<?> beanType) {
            return super.isHandler(beanType) && !AnnotatedElementUtils.hasAnnotation(beanType,
                FeignClient.class);
        }
    }

//  @Bean
//  public OkHttpClient.Builder okHttpClientBuilder(HttpsClientConfig clientConfig) {
//    TrustManager[] trustManagers = HttpsUtils.buildTrustManagers();
//    return new OkHttpClient()
//        .newBuilder()
//        .addInterceptor(new BasicLoggingInterceptor())
//        //      .cache(cache) // configure cache
//        //      .proxy(proxy) // configure proxy
//        //      .certificatePinner(certificatePinner) // certificate pinning
//        //      .addNetworkInterceptor(interceptor) // network level interceptor
//        //      .authenticator(authenticator) // authenticator for requests (it supports similar
//        // use-cases as "Authorization header" earlier
//        .callTimeout(
//            clientConfig.getCallTimeout(), TimeUnit.SECONDS) // default timeout for complete calls
//        .readTimeout(
//            clientConfig.getReadTimeout(),
//            TimeUnit.SECONDS) // default read timeout for new connections
//        .writeTimeout(
//            clientConfig.getWriteTimeout(),
//            TimeUnit.SECONDS) // default write timeout for new connections
//        //      .dns(dns) // DNS service used to lookup IP addresses for hostnames
//        //      .followRedirects(true) // follow requests redirects
//        //      .followSslRedirects(true) // follow HTTP tp HTTPS redirects
//        //      .connectionPool(connectionPool) // connection pool used to recycle HTTP and HTTPS
//        // connections
//        //      .retryOnConnectionFailure(true) // retry or not when a connectivity problem is
//        // encountered
//        //      .cookieJar(cookieJar) // cookie manager
//        //      .dispatcher(dispatcher) // dispatcher used to set policy and execute asynchronous
//        // requests
//        .sslSocketFactory(
//            HttpsUtils.createSSLSocketFactory(trustManagers), (X509TrustManager) trustManagers[0])
//        .hostnameVerifier((hostName, session) -> StringUtils.isNotBlank(hostName))
//        // 设置连接池  最大连接数量  , 持续存活的连接
//        .connectionPool(
//            new ConnectionPool(
//                clientConfig.getConnectionTimeout(),
//                clientConfig.getKeepAliveTimeout(),
//                TimeUnit.MINUTES));
//  }
}
