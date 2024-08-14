package me.boot.mq.cloud.stream;

import me.boot.mq.cloud.stream.source.OutputSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(OutputSource.class)
@SpringBootApplication
public class CloudStreamApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudStreamApplication.class, args);
    }

}
