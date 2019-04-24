package com.smart.security.example;

import com.smart.security.autoconfigure.EnableSmartSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 * @author guwenchang
 * @date 2019-04-23 19:40
 */
@SpringBootApplication
@EnableSmartSecurity
public class SmartSecurityExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartSecurityExampleApplication.class, args);
    }

}
