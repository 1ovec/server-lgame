package com.lbd.lgame.portal.component;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.lbd.lgame.common.exception.CustomException;
import com.lbd.lgame.model.User;
import com.lbd.lgame.portal.contsant.Params;
import com.lbd.lgame.portal.service.UserService;



/**
 * 用户权限拦截
 *       发起请求,进入拦截器链，运行所有拦截器的preHandle方法，
 *     1.当preHandle方法返回false时，从当前拦截器往回执行所有拦截器的afterCompletion方法，再退出拦截器链。
 *     2.当preHandle方法全为true时，执行下一个拦截器,直到所有拦截器执行完。再运行被拦截的Controller。
 *      然后进入拦截器链，运行所有拦截器的postHandle方法,完后从最后一个拦截器往回执行所有拦截器的afterCompletion方法.
 *      当有拦截器抛出异常时,会从当前拦截器往回执行所有拦截器的afterCompletion方法
 */
@Component
@ConfigurationProperties(prefix = "url")
public class UserInterceptor extends HandlerInterceptorAdapter {
	
	    private static final Logger log = LoggerFactory.getLogger(UserInterceptor.class);
	    
	    //request参数用户对象
	    public String requestUser;
	    
	    @Autowired
	    private UserService userService;
	    
	    private List<String> excludeUrl;
	    
	    @Override
	    public void afterCompletion(HttpServletRequest request,
	            HttpServletResponse response, Object handler, Exception ex)
	            throws Exception {
	        log.debug(" ==> afterCompletion");

	        super.afterCompletion(request, response, handler, ex);
	    }

	    @Override
	    public void postHandle(HttpServletRequest request,
	            HttpServletResponse response, Object handler,
	            ModelAndView modelAndView) throws Exception {
	        log.debug(" ==> postHandle");
	        super.postHandle(request, response, handler, modelAndView);
	    }

	    @Override
	    public boolean preHandle(HttpServletRequest request,
	            HttpServletResponse response, Object handler) throws Exception {
	        log.info("进入拦截器  ==> preHandle");
	        
	        log.info("请求体的MIME类型:{}",request.getContentType());
	        
	        String characterEncoding = request.getCharacterEncoding();
	        log.info("characterEncoding_" + characterEncoding);
	        
	        // 当前访问的链接
	        String currentUrl = request.getServletPath();
	        
	        log.info("currentUrl ==> " + currentUrl);
	        log.info("excludeUrl ==>" + excludeUrl.toString());
	        // 非拦截页面
	        if(excludeUrl.contains(currentUrl)){
	        	 	return super.preHandle(request, response, handler);
	        }	        
	        //手机号码
	        String userTel = request.getParameter("userTel");
	        log.info("userTel:{}",userTel);
	        if(StringUtils.isBlank(userTel)) {
	        	log.error("手机号码不存在：userTel:{}",userTel);									
				throw new CustomException("手机号码不存在");
	        }
	        //根据用户的手机号验证用户是否合法
	        User user = userService.queryUserByUserTel(userTel);
	        if (user == null) {
	            log.error("userTel:{},验证用户未通过",userTel);
	            throw new CustomException("用户手机号验证未通过。") ;
	        }
	        
	        request.setAttribute(Params.REQUEST_USER, user);	        
	        return super.preHandle(request, response, handler);
	    }
	    	    
		public List<String> getExcludeUrl() {
			return excludeUrl;
		}
		
		public void setExcludeUrl(List<String> excludeUrl) {
			this.excludeUrl = excludeUrl;
		}	    	    
}