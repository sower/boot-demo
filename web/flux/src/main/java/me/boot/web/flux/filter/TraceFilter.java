package me.boot.web.flux.filter;

import java.util.UUID;
import me.boot.base.constant.Constants;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * TraceFilter
 *
 * @since 2024/03/30
 **/
@Component
public class TraceFilter implements WebFilter {

    @NotNull
    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        serverWebExchange.getResponse().getHeaders()
            .add(Constants.TRACE_ID, UUID.randomUUID().toString());
        return webFilterChain.filter(serverWebExchange);
    }

}
