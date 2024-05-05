package me.boot.web.mvc.config;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

/**
 * WebConfig
 *
 * @since 2023/06/25
 */
@Configuration
public class WebConfig {


//    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(1000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("After request => [");
        return filter;
    }

    /**
     * 函数式路由端点
     */

    @Tag(name = "func")
    @RouterOperation(path = "/hello", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @Bean
    public RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route()
            .GET("/hello", RequestPredicates.accept(MediaType.ALL),
                request -> ServerResponse.ok().body("Hello World")).build();
    }
}
