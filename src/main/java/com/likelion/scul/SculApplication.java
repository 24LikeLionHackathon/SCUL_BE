package com.likelion.scul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SculApplication {

    public static void main(String[] args) {
        SpringApplication.run(SculApplication.class, args);
    }

}
