package com.ldb.lgame.sms.config;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ldb.lgame.sms.component.FeignRequestInterceptor;


/**
 * 微服务调用 此配置配置在需要调用其他服务的项目中
 * @author zhouhua
 *
 */
@Configuration
public class FeignConfig {
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
    
    @Bean
    RequestInterceptor requestInterceptor() {
        return new FeignRequestInterceptor();
    }
}
