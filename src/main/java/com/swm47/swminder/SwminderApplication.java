package com.swm47.swminder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SwminderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwminderApplication.class, args);
    }

}
