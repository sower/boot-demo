package me.boot.web.flux.filter;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * 函数式路由端点过滤器
 *
 * @since 2024/03/30
 **/
@Component
public class ExampleHandlerFilterFunction implements
    HandlerFilterFunction<ServerResponse, ServerResponse> {

    @NotNull
    @Override
    public Mono<ServerResponse> filter(ServerRequest serverRequest,
        @NotNull HandlerFunction<ServerResponse> handlerFunction) {
        if (serverRequest.pathVariable("name").equalsIgnoreCase("test")) {
            return ServerResponse.status(HttpStatus.FORBIDDEN).build();
        }
        return handlerFunction.handle(serverRequest);
    }


}
