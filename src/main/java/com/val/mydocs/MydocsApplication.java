package com.val.mydocs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MydocsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MydocsApplication.class, args);
    }

}
