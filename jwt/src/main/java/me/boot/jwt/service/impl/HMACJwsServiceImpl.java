package me.boot.jwt.service.impl;

import com.nimbusds.jose.Payload;
import java.util.Map;
import me.boot.jwt.config.JwtProperties;
import me.boot.jwt.service.JwtService;
import me.boot.jwt.util.JwtUtils;
import org.springframework.util.StringUtils;

/**
 * HMAC JWS Service
 *
 * @since 2024/03/06
 **/
public class HMACJwsServiceImpl implements JwtService {

    private final String secretKey;

    public HMACJwsServiceImpl(JwtProperties jwtProperties) {
        this.secretKey = jwtProperties.getHmacKey();
        if (!StringUtils.hasText(secretKey)) {
            throw new IllegalArgumentException("HmacKey not found");
        }
    }

    @Override
    public String sign(Map<String, Object> jsonObject) {
        return JwtUtils.signByHMAC(new Payload(jsonObject), secretKey);
    }

    @Override
    public Map<String, Object> verify(String token) {
        return JwtUtils.verifyJwsByHMAC(token, secretKey).toJSONObject();
    }

}
