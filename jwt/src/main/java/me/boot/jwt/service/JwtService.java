package me.boot.jwt.service;

import com.nimbusds.jose.Payload;

/**
 * JwtService
 *
 * @since 2024/03/04
 **/
public interface JwtService {

    /**
     * 对信息进行签名
     */
    String sign(Payload payload);

    /**
     * 验证并返回信息
     */
    Payload verify(String token);
}
