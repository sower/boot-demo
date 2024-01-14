package me.boot.config;

import me.boot.message.MessageReceiver;
import me.boot.message.RedisMessageListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

/**
 * @description
 * @date 2023/07/02
 **/
@Configuration
public class RedisMessageConfig {

    ChannelTopic channelTopic = new ChannelTopic("adc");
    PatternTopic patternTopic = new PatternTopic("redis.*");

    @Bean(name = "ListenerContainer")
    public RedisMessageListenerContainer container(RedisConnectionFactory factory,
        RedisMessageListener listener
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        //订阅频道
        container.addMessageListener(listener, channelTopic);
        container.addMessageListener(listener, patternTopic);
        return container;
    }


    /**
     * 监听适配器
     */
    @Bean
    public MessageListenerAdapter listenerAdapter(MessageReceiver receiver,
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer) {
        MessageListenerAdapter receiveMessage = new MessageListenerAdapter(receiver,
            "receiveMessage");
        receiveMessage.setSerializer(jackson2JsonRedisSerializer);
        return receiveMessage;
    }

    @Bean(name = "ListenerAdapterContainer")
    public RedisMessageListenerContainer container(RedisConnectionFactory factory,
        MessageListenerAdapter adapter
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        // 一次订阅多个匹配的频道
        container.addMessageListener(adapter, channelTopic);
        container.addMessageListener(adapter, patternTopic);
        return container;
    }
}