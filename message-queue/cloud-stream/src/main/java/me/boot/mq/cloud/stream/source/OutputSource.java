package me.boot.mq.cloud.stream.source;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * OutputSource
 *
 * @since 2024/06/18
 **/
public interface OutputSource {

    @Output("demo01-output")
    MessageChannel demo01Output();
}
