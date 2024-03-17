package me.boot.web.interceptor;

import com.nimbusds.jose.Payload;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.boot.base.context.BootContext;
import me.boot.base.context.BootContextHolder;
import me.boot.jwt.config.JwtProperties;
import me.boot.jwt.service.JwtService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

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

    private final JwtProperties jwtProperties;

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("int {} : {}", this.getClass().getName(), filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
        FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader(jwtProperties.getTokenName());
        if (StringUtils.isNotBlank(token)) {
            String jwt = StringUtils.removeStart(token, "Bearer ");
            Payload payload = jwtService.verify(jwt);
            BootContext context = BootContextHolder.getContext();
            context.setProperties(payload.toJSONObject());
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        BootContextHolder.clearContext();
    }
}
