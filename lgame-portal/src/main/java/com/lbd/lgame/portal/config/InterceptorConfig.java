package com.lbd.lgame.portal.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.lbd.lgame.portal.component.UserInterceptor;
import com.lbd.lgame.portal.resolver.UserArgumentResolver;


/**
 * @author Administrator
 *
 */
@SuppressWarnings("deprecation")
@Configuration
@ConfigurationProperties(prefix = "url")
public class InterceptorConfig extends WebMvcConfigurerAdapter{
	/**
	 * 拦截器配置
	 *
	 */	
	@Autowired
	private UserInterceptor userInterceptor;
	//排除路径
	private List<String> excludeUrl;
	//拦截路径
    private List<String> speUrl;
	

    
	@Override
	public void addInterceptors(InterceptorRegistry registry) {		
		registry.addInterceptor(userInterceptor).addPathPatterns(speUrl)
				.excludePathPatterns(excludeUrl);				
		super.addInterceptors(registry);
	}

	@Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(new UserArgumentResolver());       
    }
	
	/* 跨域
	 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter#addCorsMappings(org.springframework.web.servlet.config.annotation.CorsRegistry)
	 */
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

	public List<String> getExcludeUrl() {
		return excludeUrl;
	}

	public void setExcludeUrl(List<String> excludeUrl) {
		this.excludeUrl = excludeUrl;
	}

	public List<String> getSpeUrl() {
		return speUrl;
	}

	public void setSpeUrl(List<String> speUrl) {
		this.speUrl = speUrl;
	}	
}
