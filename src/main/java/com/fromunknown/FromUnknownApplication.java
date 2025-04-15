package com.fromunknown;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FromUnknownApplication {

    public static void main(String[] args) {
        SpringApplication.run(FromUnknownApplication.class, args);
    }

}
