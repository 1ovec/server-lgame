package com.lbd.lgame.portal.resolver;

import org.springframework.core.MethodParameter;


import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import com.lbd.lgame.model.User;
import com.lbd.lgame.portal.contsant.Params;




/**
 * 自动装配session中currentUser对象
 * @author junqing.cao
 *
 */
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
//    private static final Logger log = LoggerFactory.getLogger(UserArgumentResolver.class); 
    

	@Override
	public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		// 在Controller中将User参数自动注入request中的attribute对象
		if (methodParameter.getParameterType().equals(User.class)) {
			// 获取request中记录的用户对象
			User user = (User) webRequest.getAttribute(Params.REQUEST_USER, RequestAttributes.SCOPE_REQUEST);
			return user;
		}
		// else
		// if (methodParameter.getParameterType().equals(Technician.class)) {
		// // 获取request中记录的技师对象
		// Technician tech = (Technician) webRequest.getAttribute(Params.REQUEST_TECH,
		// RequestAttributes.SCOPE_REQUEST);
		// return tech;
		// }
		// else if(methodParameter.getParameterType().equals(Admin.class)){
		// Admin admin=(Admin)webRequest.getAttribute(Params.CURRENT_MANAGE_ADMIN,
		// RequestAttributes.SCOPE_SESSION);
		// return admin;
		// }
		// 微信ctrl方法对象注入
		/*
		 * else if (methodParameter.getParameterType().equals(WeChatUser.class)) {
		 * WeChatUser weChatUser = (WeChatUser)
		 * webRequest.getAttribute(Params.WECHAT_SESSION_USER_KEY,
		 * RequestAttributes.SCOPE_SESSION); return weChatUser; }
		 */
		return new Object();
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		// TODO Auto-generated method stub
		return User.class.isAssignableFrom(parameter.getParameterType());
	}
}
