package me.boot.web.flux;

import me.boot.httputil.EnableHttpBin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableHttpBin
@SpringBootApplication(scanBasePackages = "me.boot")
public class WebFluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebFluxApplication.class, args);
    }

}
