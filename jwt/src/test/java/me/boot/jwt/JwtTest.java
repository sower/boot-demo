package me.boot.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import java.security.SecureRandom;
import java.text.ParseException;
import me.boot.jwt.util.JwtUtils;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("jwt")
public class JwtTest {

    public JwtTest() throws ParseException {
    }

//    @Resource
//    StringEncryptor stringEncryptor;

    String info = "Test JWT";

    Payload payload = new Payload(info) ;

    JWTClaimsSet claimsSet = JWTClaimsSet.parse(Maps.newHashMap("adc", info));


    @Test
    public void jwsByHMAC() throws ParseException {
        // Generate random 256-bit (32-byte) shared secret
        SecureRandom random = new SecureRandom();
        byte[] sharedSecret = new byte[32];
        random.nextBytes(sharedSecret);

        String secretKey = new String(sharedSecret);
        System.err.println(secretKey);
        System.err.println(secretKey.length());
        String sign = JwtUtils.signByHMAC(payload, secretKey);
        Payload expected = JwtUtils.verifyJwsByHMAC(sign, secretKey);
        assertEquals(expected.toJSONObject(), payload.toJSONObject());
        JWT parse = JWTParser.parse(sign);

        String sign2 = JwtUtils.signByHMAC(claimsSet, secretKey);
        parse = JWTParser.parse(sign2);
        assertEquals(JwtUtils.verifyJwtByHMAC(sign2, secretKey), claimsSet);
    }

    @Test
    public void jwe() throws ParseException, JOSEException {
        RSAKey rsaKey = new RSAKeyGenerator(2048)
            .keyID("123")
            .keyUse(KeyUse.ENCRYPTION)
            .generate();
        String keyJSONString = rsaKey.toJSONString();
        System.err.println(keyJSONString);
        System.err.println(keyJSONString.length());
        String token = JwtUtils.encryptJwt(claimsSet, rsaKey);
        System.err.println(token);
        System.err.println(token.length());
        JWT parse = JWTParser.parse(token);

        JWTClaimsSet jwtClaimsSet = JwtUtils.decryptJwt(token, rsaKey);
        assertEquals(jwtClaimsSet, claimsSet);
    }

//    @Test
//    public void jasypt() {
//        String secret = "boot-hmac-secretboot-hmac-secret";
//        String encrypt = stringEncryptor.encrypt(secret);
//        System.err.println(encrypt);
//        assertEquals(stringEncryptor.decrypt(encrypt), secret);
//    }


}
