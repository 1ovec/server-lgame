package com.lbd.lgame.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@EnableDiscoveryClient
@SpringBootApplication
public class ConfigApp {
	
	public static void main(String [] args) {
		try {
			System.out.print(" 微服务配置中心启动");
			SpringApplication.run(ConfigApp.class, args);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
