package me.boot.jwt.service.impl;

import com.nimbusds.jose.Payload;
import me.boot.jwt.config.JwtProperties;
import me.boot.jwt.service.JwtService;
import me.boot.jwt.util.JwtUtils;
import org.springframework.util.StringUtils;

/**
 * HMACJwtServiceImpl
 *
 * @since 2024/03/06
 **/
public class HMACJwtServiceImpl implements JwtService {

    private final String secretKey;

    public HMACJwtServiceImpl(JwtProperties jwtProperties) {
        this.secretKey = jwtProperties.getHmacKey();
        if (!StringUtils.hasText(secretKey)) {
            throw new IllegalArgumentException("HmacKey not found");
        }
    }

    @Override
    public String sign(Payload payload) {
        return JwtUtils.signByHMAC(payload, secretKey);
    }

    @Override
    public Payload verify(String token) {
        return JwtUtils.verifyJwsByHMAC(token, secretKey);
    }
}
