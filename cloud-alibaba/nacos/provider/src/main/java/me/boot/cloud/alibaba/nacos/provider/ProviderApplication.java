package me.boot.cloud.alibaba.nacos.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * LogApplication
 *
 * @since 2024/06/02
 **/
@SpringBootApplication
public class ProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }

    @RestController
    static class EchoController {
        @RequestMapping(value = "/echo", method = RequestMethod.GET)
        public String echo(String name) {
            return "Hello Nacos Discovery " + name;
        }
    }
}
