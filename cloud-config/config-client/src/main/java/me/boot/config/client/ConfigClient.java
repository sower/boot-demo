package me.boot.config.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description
 * @since 2023/12/03
 **/
@SpringBootApplication
@RestController
@RefreshScope
public class ConfigClient {

    @Value("${spring.application.app}")
    private String env;

    public static void main(String[] args) {
        SpringApplication.run(ConfigClient.class, args);
    }

    @GetMapping(
        value = "",
        produces = MediaType.TEXT_PLAIN_VALUE)
    public String whoami() {
        return String.format("Hello! You're running in %s...\n", env);
    }
}

