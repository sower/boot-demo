package me.boot.jwt.config;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

/**
 * JwtConfig
 *
 * @since 2024/03/04
 **/
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    // JWT 在 HTTP HEADER 中默认的 KEY
    private String tokenName;

    // HMAC 密钥，用于支持 HMAC 算法
    private String hmacKey;

    // JKS 密钥路径，用于支持 RSA 算法
    private String jksFileName;

    // JKS 密钥密码，用于支持 RSA 算法
    private String jksPassword;

    // 证书密码，用于支持 RSA 算法
    private String certPassword;

    // JWT 标准信息：签发人 - iss
    private String issuer;

    // JWT 标准信息：主题 - sub
    private String subject;

    // JWT 标准信息：受众 - aud
    private String audience;

    // JWT 标准信息：生效时间 - nbf，未来多长时间内生效
    @DurationUnit(ChronoUnit.MINUTES)
    private Duration notBeforeIn;

    // JWT 标准信息：生效时间 - nbf，具体哪个时间生效
    @DurationUnit(ChronoUnit.MINUTES)
    private Duration notBeforeAt;

    // JWT 标准信息：过期时间 - exp，未来多长时间内过期
    private long expiredIn;

    // JWT 标准信息：过期时间 - exp，具体哪个时间过期
    private long expiredAt;
}