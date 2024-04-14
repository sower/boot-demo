package me.boot.base.config;

import me.boot.base.manager.GuavaRateLimitManger;
import me.boot.base.manager.RateLimitManger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * BeanConfig
 *
 * @since 2024/04/11
 **/
@Configuration
public class BeanConfig {

    @Bean
    @ConditionalOnMissingBean
    public RateLimitManger guavaLimiter(){
        return new GuavaRateLimitManger();
    }

}
