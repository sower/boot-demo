package me.boot.jwt.service;

import java.util.Map;

/**
 * JwtService
 *
 * @since 2024/03/04
 **/
public interface JwtService {

    /**
     * 对信息进行签名
     */
    String sign(Map<String, Object> jsonObject);

    /**
     * 验证并返回信息
     */
    Map<String, Object> verify(String token);

}
