import java.time.Duration;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.boot.data.redisson.DataRedissonApplication;
import me.boot.data.redisson.util.RedissonUtils;
import org.junit.jupiter.api.Test;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest(classes = DataRedissonApplication.class)
public class RedissonTest {

    @Resource
    private RedissonClient client;


    @Test
    public void objectTest() {

        RKeys keys = client.getKeys();
        keys.getKeys().forEach(System.err::println);

        RedissonUtils.setStringIfAbsent("adc","mate", Duration.ofMinutes(5));
        String adc = RedissonUtils.getString("adc");
        System.err.println(adc);
        System.err.println(RedissonUtils.ttl("adc"));
        System.err.println(RedissonUtils.ttl("Rain20240114"));

        for (int i = 0; i < 3; i++) {
            System.err.println(RedissonUtils.generateId("Rain"));
        }

    }
}
