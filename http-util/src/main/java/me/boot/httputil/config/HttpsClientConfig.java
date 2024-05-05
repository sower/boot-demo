package me.boot.httputil.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 请求配置
 *
 * @since 2022/09/04
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "https")
public class HttpsClientConfig {

    int connectionTimeout;

    int keepAliveTimeout;

    int readTimeout;

    int writeTimeout;

    int callTimeout;

}
