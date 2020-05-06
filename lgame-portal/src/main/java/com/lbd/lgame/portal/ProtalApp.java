package com.lbd.lgame.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients //启用feign客户端
@EnableDiscoveryClient
@EnableAutoConfiguration
@SpringBootApplication
public class ProtalApp {
	
	 public static void main(String[] args) {
		 try {
			 System.out.println("门户管理--启动");
			 SpringApplication.run(ProtalApp.class, args);
		 } catch(Exception e) {
			 e.printStackTrace();
		 }
	       
	 }

}
