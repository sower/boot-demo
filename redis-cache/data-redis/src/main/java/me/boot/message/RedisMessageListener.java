package me.boot.message;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @description
 * @date 2023/07/02
 **/
@Component
@Slf4j
public class RedisMessageListener implements MessageListener {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 获取监听的频道
        byte[] channelByte = message.getChannel();
        // 使用字符串序列化器转换
        Object channel = redisTemplate.getStringSerializer().deserialize(channelByte);
        // 获取消息
        byte[] messageBody = message.getBody();
        // 使用值序列化器转换
        Object msg = redisTemplate.getValueSerializer().deserialize(messageBody);
        // 渠道名称转换
        String patternStr = new String(pattern);
        log.info("Listener ---pattern---: " + patternStr);
        log.info("Listener ---频道---: " + channel);
        log.info("Listener ---消息内容---: " + msg);
    }
}
