import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.boot.RedisCacheApplication;
import me.boot.util.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;

/**
 * RedisTest
 *
 * @since 2023/03/12
 */
@Slf4j
@SpringBootTest(classes = RedisCacheApplication.class)
public class RedisTest {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void objectTest() {
        redisUtils.set("mate", "test");
        log.info("mate - {}", redisUtils.get("mate"));
        Assert.isTrue(redisUtils.hasKey("mate"), "error key");
    }

    @Test
    public void stringTest() {
        stringRedisTemplate.opsForValue().set("adc", "hi");
        log.info("adc - {}", stringRedisTemplate.opsForValue().get("adc"));
        Assert.isTrue(Boolean.TRUE.equals(stringRedisTemplate.hasKey("adc")), "error key");
    }

    @Test
    public void publish() {
        // 使用convertAndSend方法向频道redisChat发布消息
        redisTemplate.convertAndSend("adc", "aaa");
        redisTemplate.convertAndSend("redis.news", "bbb");
    }
}
