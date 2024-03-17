package me.boot.util;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

/**
 * 分布式锁
 *
 * @date 2023/07/06
 **/
@Component
@Slf4j
public class LockUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void lock() throws InterruptedException {
        // 1.生成唯一 id
        String uuid = UUID.randomUUID().toString();
// 2. 抢占锁
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 10, TimeUnit.SECONDS);
        if (Boolean.TRUE.equals(lock)) {
            log.info("抢占成功：" + uuid);
            // 3.抢占成功，执行业务

            // 4.获取当前锁的值
//      String lockValue = (String) redisTemplate.opsForValue().get("lock");
            // 5.如果锁的值和设置的值相等，则清理自己的锁
//      if(uuid.equals(lockValue)) {
//        log.info("清理锁：" + lockValue);
//        redisTemplate.delete("lock");
//      }
            // 脚本解锁 原子操作
            String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
            redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Collections.singletonList("lock"),
                uuid);


        } else {
            log.info("抢占失败，等待锁释放");
            // 4.休眠一段时间
            Thread.sleep(100);
            // 5.抢占失败，等待锁释放

        }

    }

}
