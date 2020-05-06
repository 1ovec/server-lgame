package com.lbd.lgame.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class RegistryApp {
	
	public static void main(String [] args) {
		try {
			System.out.println("注册中心启动");
			SpringApplication.run(RegistryApp.class, args);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
