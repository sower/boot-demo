package me.boot.data.redisson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "me.boot")
public class DataRedissonApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataRedissonApplication.class, args);
    }

}
