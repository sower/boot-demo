package me.boot.cloud.alibaba.nacos.config.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * App
 *
 * @since 2024/06/19
 **/
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class App {

    private String version;

    private String tag;

}
