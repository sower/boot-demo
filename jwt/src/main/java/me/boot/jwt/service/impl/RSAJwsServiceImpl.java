package me.boot.jwt.service.impl;

import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import java.text.ParseException;
import java.util.Map;
import me.boot.jwt.config.JwtProperties;
import me.boot.jwt.service.JwtService;
import me.boot.jwt.util.JwtUtils;

/**
 * RSA JWS Service
 *
 * @since 2024/03/06
 **/
public class RSAJwsServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;

    private final RSAKey rsaKey;

    public RSAJwsServiceImpl(JwtProperties jwtProperties) throws ParseException {
        this.jwtProperties = jwtProperties;
        String secretKey = jwtProperties.getRseKey();
        rsaKey = RSAKey.parse(secretKey);
    }

    @Override
    public String sign(Map<String, Object> jsonObject) {
        JWTClaimsSet claimsSet = JwtProperties.buildClaimsSet(jwtProperties, jsonObject);
        return JwtUtils.encryptJwt(claimsSet, rsaKey);
    }

    @Override
    public Map<String, Object> verify(String token) {
        return JwtUtils.decryptJwt(token, rsaKey).toJSONObject();
    }

}
