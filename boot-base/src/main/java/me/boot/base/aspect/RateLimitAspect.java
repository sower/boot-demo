package me.boot.base.aspect;

import java.lang.reflect.Method;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.boot.base.annotation.RateLimit;
import me.boot.base.annotation.RateLimit.Strategy;
import me.boot.base.manager.RateLimitManger;
import me.boot.base.property.RateLimitProperty;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.stereotype.Component;

/**
 * DefaultRateLimitAspect
 *
 * @since 2024/04/11
 **/
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

    private final RateLimitManger rateLimitManger;

    @Before("@within(rateLimit) && !@annotation(me.boot.base.annotation.RateLimit)")
    public void classPointcut(JoinPoint point, RateLimit rateLimit) {
        System.err.println("classPointcut");
        limitRate(point, rateLimit);
    }

    @Before("@annotation(rateLimit)")
    public void methodPointcut(JoinPoint point, RateLimit rateLimit) {
        System.err.println("methodPointcut");
        limitRate(point, rateLimit);
    }

    private void limitRate(JoinPoint point, RateLimit rateLimit) {
        if (rateLimit.qps() <= RateLimit.NOT_LIMITED) {
            return;
        }

        String key = rateLimit.key();
        if (StringUtils.isBlank(key)) {
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            key = method.getName();
        }

        long qps = rateLimit.qps();
        Duration timeout = parseTime(rateLimit.timeout());
        RateLimitProperty limitProperty = new RateLimitProperty(key, qps, timeout);
        if (Strategy.TOTAL.equals(rateLimit.strategy())) {
            if (!rateLimitManger.totalLimit(limitProperty)) {
                throw new RuntimeException("请求太快，慢点重试");
            }
        }
    }

    private Duration parseTime(String input) {
        if (StringUtils.isBlank(input)) {
            return Duration.ZERO;
        }
        return DurationStyle.detectAndParse(input);
    }
}