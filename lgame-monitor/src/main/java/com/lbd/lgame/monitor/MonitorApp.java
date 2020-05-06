package com.lbd.lgame.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import de.codecentric.boot.admin.server.config.EnableAdminServer;


/**
 * @author zhouhua
 * 2020-01-07
 * I am not responsible for this code。
 */
@EnableDiscoveryClient
@EnableAdminServer
@SpringBootApplication
public class MonitorApp {
	
	public static void main(String [] args) {
		System.out.println("监控系统启动");
		try {
			SpringApplication.run(MonitorApp.class, args);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
