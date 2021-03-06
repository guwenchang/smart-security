package com.smart.security.autoconfigure;


import com.smart.security.interceptor.AuthInterceptor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 启动配置
 * @author guwenchang
 * @date 2019-04-22 15:47
 */
@Configuration
@EnableAutoConfiguration
@Import(SmartSecurityConfiguration.class)
public class SmartSecurityAutoConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor());
    }
}