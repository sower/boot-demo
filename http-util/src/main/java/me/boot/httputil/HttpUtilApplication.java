package me.boot.httputil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "me.boot")
public class HttpUtilApplication {

    public static void main(String[] args) {
        SpringApplication.run(HttpUtilApplication.class, args);
    }

}
