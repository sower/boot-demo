package me.boot.data.redisson.manager;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import me.boot.base.manager.RateLimitManger;
import me.boot.base.property.RateLimitProperty;
import me.boot.data.redisson.util.RedissonUtils;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;

/**
 * RedissonRateLimitManger
 *
 * @since 2024/04/11
 **/
public class RedissonRateLimitManger implements RateLimitManger {

    @Override
    public boolean totalLimit(RateLimitProperty rateLimit) {
        RRateLimiter rateLimiter = RedissonUtils.getRateLimiter(rateLimit.getKey());
        rateLimiter.trySetRate(RateType.OVERALL, rateLimit.getQps(), 1, RateIntervalUnit.SECONDS);
        Duration timeout = rateLimit.getTimeout();
        return rateLimiter.tryAcquire(timeout.toMillis(), TimeUnit.MILLISECONDS);
    }

}
