package me.boot.jwt.config;

import java.text.ParseException;
import me.boot.jwt.service.JwtService;
import me.boot.jwt.service.impl.HMACJwsServiceImpl;
import me.boot.jwt.service.impl.HMACJwtServiceImpl;
import me.boot.jwt.service.impl.RSAJwsServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

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
    @ConditionalOnExpression("!'${jwt.hmac-key}'?.empty && '${jwt.effective-time}' == null")
    public JwtService hmacJwsService(JwtProperties jwtProperties) {
        return new HMACJwsServiceImpl(jwtProperties);
    }

    @Bean
    @ConditionalOnExpression("#{T(org.apache.commons.lang3.StringUtils).isNoneBlank('${jwt.hmac-key}', '${jwt.effective-time}')}")
    public JwtService hmacJwtService(JwtProperties jwtProperties) {
        return new HMACJwtServiceImpl(jwtProperties);
    }

    @Bean
    @Primary
    @ConditionalOnProperty(name = "jwt.rse-key")
    public JwtService rseJwsService(JwtProperties jwtProperties) throws ParseException {
        return new RSAJwsServiceImpl(jwtProperties);
    }

}
