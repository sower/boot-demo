package me.boot.httputil.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 请求配置
 *
 * @date 2022/09/04
 */
@Data
@Component
@ConfigurationProperties(prefix = "https")
public class HttpsClientConfig {

    int connectionTimeout;
    int keepAliveTimeout;
    int readTimeout;
    int writeTimeout;
    int callTimeout;
}
