package me.boot.web;

import me.boot.httputil.EnableHttpBin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableHttpBin
@SpringBootApplication(scanBasePackages = "me.boot")
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

}
