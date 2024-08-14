package me.boot.mq.cloud.stream.message;

import lombok.Data;

/**
 * DemoMessage
 *
 * @since 2024/06/18
 **/
@Data
public class DemoMessage {
    /**
     * 编号
     */
    private Integer id;


    /**
     * 内容
     */
    private String content;
}
