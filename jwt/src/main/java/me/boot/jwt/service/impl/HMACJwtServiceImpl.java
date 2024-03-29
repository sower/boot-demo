package me.boot.jwt.service.impl;

import com.nimbusds.jwt.JWTClaimsSet;
import java.util.Map;
import me.boot.jwt.config.JwtProperties;
import me.boot.jwt.service.JwtService;
import me.boot.jwt.util.JwtUtils;
import org.springframework.util.StringUtils;

/**
 * HMAC Jwt Service
 *
 * @since 2024/03/06
 **/
public class HMACJwtServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;

    public HMACJwtServiceImpl(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        String secretKey = jwtProperties.getHmacKey();
        if (!StringUtils.hasText(secretKey)) {
            throw new IllegalArgumentException("HmacKey not found");
        }
    }

    @Override
    public String sign(Map<String, Object> jsonObject) {
        JWTClaimsSet claimsSet = JwtProperties.buildClaimsSet(jwtProperties, jsonObject);
        return JwtUtils.signByHMAC(claimsSet, jwtProperties.getHmacKey());
    }

    @Override
    public Map<String, Object> verify(String token) {
        return JwtUtils.verifyJwsByHMAC(token, jwtProperties.getHmacKey()).toJSONObject();
    }

}
