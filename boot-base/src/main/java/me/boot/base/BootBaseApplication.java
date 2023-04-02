package me.boot.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "me.boot")
public class BootBaseApplication {

  public static void main(String[] args) {
    SpringApplication.run(BootBaseApplication.class, args);
  }

}
