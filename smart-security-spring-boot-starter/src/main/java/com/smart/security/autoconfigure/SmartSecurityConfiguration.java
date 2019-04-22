package com.smart.security.autoconfigure;

import com.smart.security.config.SmartSecurityProperties;
import com.smart.security.jwt.JwtOperator;
import com.smart.security.jwt.UserOperator;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类
 * @author guwenchang
 * @date 2019-04-22 15:48
 */
@Configuration
@AutoConfigureBefore(SmartSecurityAutoConfiguration.class)
@EnableConfigurationProperties(SmartSecurityProperties.class)
public class SmartSecurityConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public JwtOperator jwtOperator(SmartSecurityProperties smartSecurityProperties) {
        return new JwtOperator(smartSecurityProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public UserOperator userOperator(JwtOperator jwtOperator) {
        return new UserOperator(jwtOperator);
    }

}

