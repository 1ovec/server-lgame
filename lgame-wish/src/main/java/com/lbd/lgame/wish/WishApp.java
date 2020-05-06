/**
 * zhouhua
 * 2020-01-06
 * I am not responsible for this code。
 */
package com.lbd.lgame.wish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author zhouhua
 * 2020-01-06
 * I am not responsible for this code。
 */
@EnableFeignClients //启用feign客户端
@EnableDiscoveryClient
@SpringBootApplication
public class WishApp {
	
	public static void main(String [] args) {
		System.out.println("wish==启动");
		try {
			SpringApplication.run(WishApp.class, args);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
