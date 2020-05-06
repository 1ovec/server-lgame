package com.ldb.lgame.sms;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * lgame-sms
 *
 */
@EnableFeignClients //启用feign客户端
@EnableDiscoveryClient
@EnableAutoConfiguration  //申明事物
@SpringBootApplication
public class SmsApp 
{
    public static void main( String[] args )
    {	
        System.out.println( "run lgame-sms" );
        try {
        	 SpringApplication.run(SmsApp.class, args);
        } catch(Exception e) {
        	e.printStackTrace();
        }      
    }
}
