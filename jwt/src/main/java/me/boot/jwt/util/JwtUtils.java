package me.boot.jwt.util;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSHeader.Builder;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * JwtUtils
 *
 * @since 2024/03/04
 **/
@Slf4j
public abstract class JwtUtils {

    public static final JWSHeader JWS_HS_HEADER = new JWSHeader(JWSAlgorithm.HS256);

    /**
     * 使用 HMAC 算法签名信息（Payload 中只包含私有信息）
     */
    public static String signByHMAC(Payload payload, String key) {
        JWSObject jwsObject = new JWSObject(JWS_HS_HEADER, payload);
        return signByHMAC(jwsObject, key);
    }


    public static String signByHMAC(JWTClaimsSet claimsSet, String key) {
        SignedJWT signedJWT = new SignedJWT(JWS_HS_HEADER, claimsSet);
        return signByHMAC(signedJWT, key);
    }

    @SneakyThrows
    private static String signByHMAC(JWSObject jwsObject, String key) {
        // Create HMAC signer
        JWSSigner signer = new MACSigner(key);
        // Apply the HMAC protection
        jwsObject.sign(signer);
        // 生成 token
        return jwsObject.serialize();
    }

    /**
     * 使用 RSA 算法签名信息（Payload 中只包含私有信息）
     */
    public static String signByRSA(Payload payload, RSAKey rsaKey) {
        JWSHeader header = new Builder(JWSAlgorithm.RS256).keyID(rsaKey.getKeyID()).build();
        JWSObject jwsObject = new JWSObject(header, payload);
        return signByRSA(rsaKey, jwsObject);
    }

    public static String signByRSA(JWTClaimsSet claimsSet, RSAKey rsaKey) {
        JWSHeader header = new Builder(JWSAlgorithm.RS256).keyID(rsaKey.getKeyID()).build();
        JWSObject jwsObject = new SignedJWT(header, claimsSet);
        return signByRSA(rsaKey, jwsObject);
    }

    @SneakyThrows
    private static String signByRSA(RSAKey rsaKey, JWSObject jwsObject) {
        // 进行加密
        JWSSigner signer = new RSASSASigner(rsaKey);
        jwsObject.sign(signer);
        return jwsObject.serialize();
    }

    /**
     * 使用 HMAC 算法验证 token（Payload 中只包含私有信息）
     */
    @SneakyThrows
    public static Payload verifyJwsByHMAC(String token, String key) {
        JWSVerifier jwsVerifier = new MACVerifier(key);
        JWSObject jwsObject = JWSObject.parse(token);
        if (jwsObject.verify(jwsVerifier)) {
            return jwsObject.getPayload();
        }
        // 验证token失败
        throw new BadJOSEException("验证token失败");
    }

    @SneakyThrows
    public static JWTClaimsSet verifyJwtByHMAC(String token, String key) {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier jwsVerifier = new MACVerifier(key);
        if (signedJWT.verify(jwsVerifier)) {
            return signedJWT.getJWTClaimsSet();
        }
        // 验证token失败
        throw new BadJOSEException("验证token失败");
    }

    /**
     * 使用 RSA 算法验证 token（Payload 中只包含私有信息）
     */
    @SneakyThrows
    public static Payload verifyJwsByRSA(String token, RSAKey publicRSAKey) {
        JWSObject jwsObject = JWSObject.parse(token);
        JWSVerifier jwsVerifier = new RSASSAVerifier(publicRSAKey);
        // 验证数据
        if (jwsObject.verify(jwsVerifier)) {
            return jwsObject.getPayload();
        }
        throw new BadJOSEException("验证token失败");
    }

    @SneakyThrows
    public static JWTClaimsSet verifyJwtByRSA(String token, RSAKey publicRSAKey) {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier jwsVerifier = new RSASSAVerifier(publicRSAKey);
        // 验证数据
        if (signedJWT.verify(jwsVerifier)) {
            return signedJWT.getJWTClaimsSet();
        }
        throw new BadJOSEException("验证token失败");
    }


    @SneakyThrows
    public static String encryptJwt(JWTClaimsSet jwtClaims, RSAKey publicKey) {
        JWEHeader header = new JWEHeader(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A128GCM);
        // Create the encrypted JWT object
        EncryptedJWT jwt = new EncryptedJWT(header, jwtClaims);

        // Create an encrypter with the specified public RSA key
        RSAEncrypter encrypter = new RSAEncrypter(publicKey);

        // Do the actual encryption
        jwt.encrypt(encrypter);

        // Serialise to JWT compact form
        return jwt.serialize();
    }

    @SneakyThrows
    public static JWTClaimsSet decryptJwt(String token, RSAKey privateKey) {
        // Create the encrypted JWT object
        EncryptedJWT jwt = EncryptedJWT.parse(token);

        /// Create a decrypter with the specified private RSA key
        RSADecrypter decrypter = new RSADecrypter(privateKey);
        // Decrypt
        jwt.decrypt(decrypter);
        return jwt.getJWTClaimsSet();
    }
}
