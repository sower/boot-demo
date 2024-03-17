package me.boot.jwt.config;

import me.boot.jwt.service.JwtService;
import me.boot.jwt.service.impl.HMACJwtServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * JwtAutoConfiguration
 *
 * @since 2024/03/17
 **/
@AutoConfiguration
@ConditionalOnClass(JwtService.class)
@EnableConfigurationProperties({JwtProperties.class})
public class JwtAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = "jwt.hmac-key")
    public JwtService hmacJwtService(JwtProperties jwtProperties) {
        return new HMACJwtServiceImpl(jwtProperties);
    }

}
