package me.boot.mq.rabbit.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * QueueMessageListener
 *
 * @since 2024/02/14
 **/
@Component
public class QueueMessageListener {

    @RabbitListener(queues = "myQueue")
    public void listen(String in) {
        System.err.println("Message read from myQueue : " + in);
    }
}
