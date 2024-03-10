package me.boot.mq.rabbit.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitmqConfig
 *
 * @since 2024/02/14
 **/
@Configuration
public class RabbitmqConfig {

    @Bean
    public Queue myQueue() {
        return new Queue("myQueue", false);
    }


//    @Bean
//    public MessageConverter createMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
}
