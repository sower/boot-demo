import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.boot.RedisCacheApplication;
import me.boot.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

/**
 * @description
 * @date 2023/03/12
 **/
@Slf4j
@SpringBootTest(classes = RedisCacheApplication.class)
public class RedisTest {

  @Resource
  private RedisUtil redisUtil;

  @Test
  public void stringTest() {
    redisUtil.set("adc", "test");
    log.info("adc - {}", redisUtil.get("adc"));
    Assert.isTrue(redisUtil.hasKey("adc"), "error key");
  }
}
