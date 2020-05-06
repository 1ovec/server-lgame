package com.lbd.lgame.portal.contsant;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * contsant
 * @author Administrator
 *
 */
@Component
public class Params implements InitializingBean {

    @Value("${url.requestUser}")
    private String requestUser;

    
    /**
     * request参数用户对象
     */
    public static String REQUEST_USER;

    @Override
    public void afterPropertiesSet() throws Exception {
    	REQUEST_USER=requestUser;
    }
}

