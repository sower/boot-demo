package me.boot.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nimbusds.jose.Payload;
import com.nimbusds.jwt.JWTClaimsSet;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Map;
import javax.annotation.Resource;
import me.boot.jwt.util.JwtUtils;
import org.assertj.core.util.Maps;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("jwt")
public class JwtTest {

    @Resource
    StringEncryptor stringEncryptor;

    String info = "Test JWT";

    Payload payload = new Payload(info) ;

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
        assertEquals(JwtUtils.verifyJwsByHMAC(sign, secretKey), info);

        Map<String, Object> map = Maps.newHashMap("adc", info);
        JWTClaimsSet claimsSet = JWTClaimsSet.parse(map);
        String sign2 = JwtUtils.signByHMAC(claimsSet, secretKey);
        assertEquals(JwtUtils.verifyJwtByHMAC(sign2, secretKey), claimsSet);

    }

    @Test
    public void jasypt() {
        String secret = "boot-hmac-secretboot-hmac-secret";
        String encrypt = stringEncryptor.encrypt(secret);
        System.err.println(encrypt);
        assertEquals(stringEncryptor.decrypt(encrypt), secret);
    }


}
