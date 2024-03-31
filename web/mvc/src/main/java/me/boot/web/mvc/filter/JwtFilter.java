package me.boot.web.mvc.filter;

import com.nimbusds.jwt.JWTClaimNames;
import java.io.IOException;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.boot.base.context.BootContext;
import me.boot.base.context.BootContextHolder;
import me.boot.jwt.service.JwtService;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * JwtInterceptor
 *
 * @since 2024/03/17
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Resource(name = "handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        @NotNull HttpServletResponse response,
        @NotNull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(token)) {
            String jwt = StringUtils.removeStart(token, "Bearer ");
            Map<String, Object> payload = jwtService.verify(jwt);
            long expirationTime = 1000 * (long) payload.get(JWTClaimNames.EXPIRATION_TIME);
            if (System.currentTimeMillis() > expirationTime) {
                exceptionResolver.resolveException(request, response,
                    null, new ServletException("The token has expired"));
                return;
            }
            BootContext context = BootContextHolder.getContext();
            context.setProperties(payload);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        BootContextHolder.clearContext();
    }
}
