package com.lbd.lgame.redis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;



/**
 * lgame-redis
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
public class RedisApp 
{
    public static void main( String[] args )
    {	
        System.out.println( "run lgame-redis" );
        try {
        	 SpringApplication.run(RedisApp.class, args);
        } catch(Exception e) {
        	e.printStackTrace();
        }      
    }
}
