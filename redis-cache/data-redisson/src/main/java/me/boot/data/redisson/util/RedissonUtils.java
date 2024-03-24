package me.boot.data.redisson.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import me.boot.base.context.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.lang.NonNull;

/**
 * util
 *
 * @since 2024/01/13
 **/
public abstract class RedissonUtils {

    private static final RedissonClient redisson = SpringContextHolder.getBean(
        RedissonClient.class);

    private static final String REDIS_KEY_PREFIX = SpringContextHolder.getProperty(
        "spring.cache.redis.key-prefix");

    private static final NumberFormat NUMBER_FORMAT = new DecimalFormat("000000");


    /**
     * 业务每日分布自增ID
     *
     * @param bizName 业务名
     * @return {@link String}
     */
    public static String generateId(String bizName) {
        String now = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String prefix = bizName + now;
        String key = normalize(prefix);

        // 通过redis的自增获取序号
        RAtomicLong atomicLong = redisson.getAtomicLong(key);
        if (!atomicLong.isExists()) {
            atomicLong.set(0);
            // 设置到期时间1天
            atomicLong.expire(Duration.ofDays(1));
        }
        long num = atomicLong.incrementAndGet();
        return prefix + NUMBER_FORMAT.format(num);
    }


    private static <T> RBucket<T> getBucket(@NonNull String key) {
        return redisson.getBucket(normalize(key));
    }

    private static RBucket<String> getStringRBucket(String key) {
        return redisson.getBucket(normalize(key), StringCodec.INSTANCE);
    }


    private static String normalize(String key) {
        return StringUtils.prependIfMissing(key, REDIS_KEY_PREFIX);
    }

    /**
     * 读取缓存
     *
     * @param key 缓存key
     * @return 缓存返回值
     */
    public static <T> T get(@NonNull String key) {
        RBucket<T> bucket = getBucket(key);
        return bucket.get();
    }


    /**
     * 以string的方式读取缓存
     *
     * @param key 缓存key
     * @return 缓存返回值
     */
    public static String getString(String key) {
        RBucket<String> bucket = getStringRBucket(key);
        return bucket.get();
    }

    /**
     * 设置缓存
     *
     * @param key   缓存key
     * @param value 缓存值
     */
    public static <T> void set(String key, T value, Duration duration) {
        RBucket<T> bucket = getBucket(key);
        bucket.set(value, duration);
    }

    /**
     * 以string的方式设置缓存
     */
    public static void setString(String key, String value, Duration duration) {
        RBucket<String> bucket = getStringRBucket(key);
        bucket.set(value, duration);
    }

    /**
     * 如果不存在则写入缓存
     */
    public static <T> boolean setIfAbsent(String key, T value, Duration duration) {
        RBucket<T> bucket = getBucket(key);
        return bucket.setIfAbsent(value, duration);
    }

    /**
     * 如果不存在则写入string缓存
     *
     * @param key   缓存key
     * @param value 缓存值
     */
    public static boolean setStringIfAbsent(String key, String value, Duration duration) {
        RBucket<String> bucket = getStringRBucket(key);
        return bucket.setIfAbsent(value, duration);
    }

    /**
     * 获取锁对象
     *
     * @param key /
     * @return {@link RLock}
     */
    public static RLock getLock(String key) {
        return redisson.getLock(normalize(key));
    }


    /**
     * 移除缓存
     */
    public static void remove(String key) {
        getBucket(key).delete();
    }

    /**
     * 判断缓存是否存在
     */
    public static boolean exists(String key) {
        return getBucket(key).isExists();
    }

    /**
     * 获取缓存失效时间
     */
    public static long ttl(String key) {
        return getBucket(key).remainTimeToLive();
    }
}
