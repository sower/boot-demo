package me.boot.web.filter;

import com.nimbusds.jwt.JWTClaimNames;
import java.io.IOException;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.boot.base.context.BootContext;
import me.boot.base.context.BootContextHolder;
import me.boot.jwt.service.JwtService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * JwtInterceptor
 *
 * @since 2024/03/17
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtService jwtService;

    @Resource(name = "handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("int {} : {}", this.getClass().getName(), filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
        FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(token)) {
            String jwt = StringUtils.removeStart(token, "Bearer ");
            Map<String, Object> payload = jwtService.verify(jwt);
            long expirationTime = 1000 * (long) payload.get(JWTClaimNames.EXPIRATION_TIME);
            if (System.currentTimeMillis() > expirationTime) {
                exceptionResolver.resolveException(request, (HttpServletResponse) servletResponse,
                    null, new ServletException("The token has expired"));
                return;
            }
            BootContext context = BootContextHolder.getContext();
            context.setProperties(payload);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        BootContextHolder.clearContext();
    }
}
