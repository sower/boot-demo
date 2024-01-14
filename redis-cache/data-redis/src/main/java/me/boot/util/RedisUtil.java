package me.boot.util;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @date 2023/03/12
 **/
@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 给一个指定的 key 值附加过期时间
     */
    public Boolean expire(String key, long second) {
        return redisTemplate.expire(key, second, TimeUnit.SECONDS);
    }

    /**
     * 根据key 获取过期时间
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }


    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }


    public Boolean persist(String key) {
        return redisTemplate.boundValueOps(key).persist();
    }

    //- - - - - - - - - - - - - - - - - - - - -  String类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * 根据key获取值
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 将值放入缓存
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 将值放入缓存并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) -1为无期限
     */
    public void set(String key, Object value, long time) {
        if (time > 0) {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * 批量添加 key (重复的键会覆盖)
     */
    public void batchSet(Map<String, String> keyAndValue) {
        redisTemplate.opsForValue().multiSet(keyAndValue);
    }

    /**
     * 批量添加 key-value 只有在键不存在时,才添加 map 中只要有一个key存在,则全部不添加
     */
    public void batchSetIfAbsent(Map<String, String> keyAndValue) {
        redisTemplate.opsForValue().multiSetIfAbsent(keyAndValue);
    }

    /**
     * 对一个 key-value 的值进行加减操作, 如果该 key 不存在 将创建一个key 并赋值该 number 如果 key 存在,但 value 不是长整型 ,将报错
     */
    public Long increment(String key, long number) {
        return redisTemplate.opsForValue().increment(key, number);
    }

    /**
     * 对一个 key-value 的值进行加减操作, 如果该 key 不存在 将创建一个key 并赋值该 number 如果 key 存在,但 value 不是 纯数字 ,将报错
     */
    public Double increment(String key, double number) {
        return redisTemplate.opsForValue().increment(key, number);
    }

    //- - - - - - - - - - - - - - - - - - - - -  set类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * 将数据放入set缓存
     *
     * @param key 键
     */
    public void sSet(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    /**
     * 获取变量中的值
     *
     * @param key 键
     */
    public Set<Object> members(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 随机获取变量中指定个数的元素
     *
     * @param key   键
     * @param count 值
     */
    public void randomMembers(String key, long count) {
        redisTemplate.opsForSet().randomMembers(key, count);
    }

    /**
     * 随机获取变量中的元素
     *
     * @param key 键
     */
    public Object randomMember(String key) {
        return redisTemplate.opsForSet().randomMember(key);
    }

    /**
     * 弹出变量中的元素
     *
     * @param key 键
     */
    public Object pop(String key) {
        return redisTemplate.opsForSet().pop("setValue");
    }

    /**
     * 获取变量中值的长度
     *
     * @param key 键
     */
    public Long size(String key) {
        return redisTemplate.opsForSet().size(key);
    }


    /**
     * 检查给定的元素是否在变量中。
     *
     * @param key 键
     * @param obj 元素对象
     */
    public Boolean isMember(String key, Object obj) {
        return redisTemplate.opsForSet().isMember(key, obj);
    }

    /**
     * 转移变量的元素值到目的变量。
     *
     * @param key     键
     * @param value   元素对象
     * @param destKey 元素对象
     */
    public Boolean move(String key, String value, String destKey) {
        return redisTemplate.opsForSet().move(key, value, destKey);
    }

    /**
     * 批量移除set缓存中元素
     *
     * @param key    键
     * @param values 值
     */
    public void remove(String key, Object... values) {
        redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 通过给定的key求2个set变量的差值
     *
     * @param key     键
     * @param destKey 键
     */
    public Set<Object> difference(String key, String destKey) {
        return redisTemplate.opsForSet().difference(key, destKey);
    }

    //- - - - - - - - - - - - - - - - - - - - -  hash类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * 加入缓存
     *
     * @param key 键
     * @param map 键
     */
    public void add(String key, Map<String, String> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 获取 key 下的 所有  hashKey 和 value
     *
     * @param key 键
     */
    public Map<Object, Object> getHashEntries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 验证指定 key 下 有没有指定的 hashKey
     */
    public Boolean hasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * 获取指定key的值string
     */
    public Object getMapString(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }


    /**
     * 弹出元素并删除
     *
     * @param key 键
     */
    public Object popValue(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * 删除指定 hash 的 HashKey
     */
    public Long delete(String key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    /**
     * 给指定 hash 的 hashKey 做增减操作
     */
    public Long increment(String key, String hashKey, long number) {
        return redisTemplate.opsForHash().increment(key, hashKey, number);
    }

    /**
     * 给指定 hash 的 hashKey 做增减操作
     */
    public Double increment(String key, String hashKey, Double number) {
        return redisTemplate.opsForHash().increment(key, hashKey, number);
    }

    /**
     * 获取 key 下的 所有 hashKey 字段
     */
    public Set<Object> hashKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 获取指定 hash 下面的 键值对 数量
     *
     * @param key
     * @return
     */
    public Long hashSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    //- - - - - - - - - - - - - - - - - - - - -  list类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * 在变量左边添加元素值
     */
    public void leftPush(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 获取集合指定位置的值。
     */
    public Object index(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 获取指定区间的值。
     */
    public List<Object> range(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 把最后一个参数值放到指定集合的第一个出现中间参数的前面， 如果中间参数值存在的话。
     */
    public void leftPush(String key, String pivot, String value) {
        redisTemplate.opsForList().leftPush(key, pivot, value);
    }

    /**
     * 向左边批量添加参数元素。
     */
    public void leftPushAll(String key, String... values) {
        redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 向集合最右边添加元素。
     */
    public void leftPushAll(String key, String value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 向左边批量添加参数元素。
     */
    public void rightPushAll(String key, String... values) {
        redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 向已存在的集合中添加元素。
     */
    public void rightPushIfPresent(String key, Object value) {
        redisTemplate.opsForList().rightPushIfPresent(key, value);
    }

    /**
     * 向已存在的集合中添加元素。
     */
    public Long listLength(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 移除集合中的左边第一个元素。
     */
    public void leftPop(String key) {
        redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 移除集合中左边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出。
     */
    public void leftPop(String key, long timeout, TimeUnit unit) {
        redisTemplate.opsForList().leftPop(key, timeout, unit);
    }

    /**
     * 移除集合中右边的元素。
     */
    public void rightPop(String key) {
        redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 移除集合中右边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出。
     */
    public void rightPop(String key, long timeout, TimeUnit unit) {
        redisTemplate.opsForList().rightPop(key, timeout, unit);
    }
}
