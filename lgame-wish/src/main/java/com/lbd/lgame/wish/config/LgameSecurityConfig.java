package com.lbd.lgame.wish.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.lbd.lgame.wish.service.UserService;
import com.ldb.lgame.security.config.SecurityConfig;

/**
 * LgameSecurity 模块配置
 * @author zhouhua
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class LgameSecurityConfig extends SecurityConfig{

	@Autowired
	private UserService userService;
	
	private static final Logger log = LoggerFactory.getLogger(LgameSecurityConfig.class);
	
    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
    	log.info("获取登录用户信息");
        return username -> userService.loadUserByUsername(username,"1");
    }
}
