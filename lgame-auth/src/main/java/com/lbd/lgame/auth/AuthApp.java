package com.lbd.lgame.auth;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;



/**
 * lgame-sms
 *
 */
@EnableDiscoveryClient
@SpringBootApplication
public class AuthApp 
{
    public static void main( String[] args )
    {	
        System.out.println( "run lgame-auth" );
        try {
        	 SpringApplication.run(AuthApp.class, args);
        } catch(Exception e) {
        	e.printStackTrace();
        }      
    }
}
