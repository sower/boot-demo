package me.boot.base.manager;

import me.boot.base.property.RateLimitProperty;

/**
 * RateLimitManger
 *
 * @since 2024/04/11
 **/
public interface RateLimitManger {

    boolean totalLimit(RateLimitProperty rateLimit);


    // boolean ipLimit(RateLimitProperty rateLimit);
}
