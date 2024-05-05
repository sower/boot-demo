package me.boot.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * MessageReceiver
 *
 * @since 2023/07/05
 */
@Component
@Slf4j
public class MessageReceiver {

    public void receiveMessage(String message, String channel) {
        log.info("Receiver ---频道---: " + channel);
        log.info("Receiver ---消息内容---: " + message);
    }
}
