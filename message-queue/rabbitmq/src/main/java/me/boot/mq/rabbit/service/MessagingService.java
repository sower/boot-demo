package me.boot.mq.rabbit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * MessagingService
 *
 * @since 2024/02/14
 **/
@Service
@RequiredArgsConstructor
public class MessagingService {

    private final RabbitTemplate rabbitTemplate;

    public void senMsg(Object msg) {
        rabbitTemplate.convertAndSend("myQueue", msg);
    }

}