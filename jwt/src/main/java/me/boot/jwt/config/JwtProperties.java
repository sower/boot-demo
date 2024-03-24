package me.boot.jwt.config;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTClaimsSet.Builder;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
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

    // HMAC 密钥，用于支持 HMAC 算法
    private String hmacKey;

    private String rseKey;

    // JWT 标准信息：签发人 - iss
    private String issuer;

    // JWT 标准信息：主题 - sub
    private String subject;

    // JWT 标准信息：受众 - aud
    private String audience;

    // 有效时间：生效时间 - 过期时间
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration effectiveTime;


    public static JWTClaimsSet buildClaimsSet(JwtProperties properties,Map<String, Object>jsonObject) {
        Date now = new Date();
        Date expirationTime = new Date(
            now.getTime() + 1000 * properties.getEffectiveTime().getSeconds());
        Builder builder = new Builder().subject(properties.getSubject())
            .issuer(properties.getIssuer()).audience(properties.getAudience()).issueTime(now)
            .notBeforeTime(now).expirationTime(expirationTime).jwtID(UUID.randomUUID().toString());
        jsonObject.forEach(builder::claim);
        return builder.build();
    }

}