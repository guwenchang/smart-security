package com.smart.security.example;

import com.smart.security.autoconfigure.EnableSmartSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableSmartSecurity
public class SmartSecurityExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartSecurityExampleApplication.class, args);
    }

}
