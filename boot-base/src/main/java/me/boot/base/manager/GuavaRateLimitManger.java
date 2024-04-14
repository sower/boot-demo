package me.boot.base.manager;

import com.google.common.util.concurrent.RateLimiter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.extern.slf4j.Slf4j;
import me.boot.base.property.RateLimitProperty;

/**
 * RateLimitManger
 *
 * @since 2024/04/11
 **/
@Slf4j
public class GuavaRateLimitManger implements RateLimitManger {

    private static final ConcurrentMap<String, RateLimiter> RATE_LIMITER_CACHE = new ConcurrentHashMap<>();

    //  ip -> key -> rate
    //    private static final ConcurrentMap<String, Map<String, RateLimiter>> IP_RATE_LIMITER_CACHE = new ConcurrentHashMap<>();
    @Override
    public boolean totalLimit(RateLimitProperty rateLimit) {
        String key = rateLimit.getKey();
        if (RATE_LIMITER_CACHE.get(key) == null) {
            // 初始化 QPS
            RATE_LIMITER_CACHE.put(key, RateLimiter.create(rateLimit.getQps()));
        }
        RateLimiter rateLimiter = RATE_LIMITER_CACHE.get(key);
        log.debug("{} of qps -> {}", key, rateLimiter.getRate());
        // 尝试获取令牌
        return rateLimiter.tryAcquire(rateLimit.getTimeout());
    }

}
