package com.lbd.lgame.portal.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.lbd.lgame.common.utils.Cipher;
import com.lbd.lgame.common.utils.Client;
import com.lbd.lgame.common.utils.JsonResult;
import com.lbd.lgame.model.User;
import com.lbd.lgame.portal.service.FeignRedisService;
import com.lbd.lgame.portal.service.FeignSmsService;
import com.lbd.lgame.portal.service.UserService;




/**
 * 用户登录注册
 * @author zhouhua
 *
 */
@Controller
@RequestMapping("/sso")
public class AppUserCtrl {
	
	private static final Logger log = LoggerFactory.getLogger(AppUserCtrl.class);
	
	@Autowired
	private UserService userService;
	
	
	/**
	 * 注册
	 * @param phoneNo
	 * @param pwd
	 * @param verifyCode
	 * @param clientType
	 * @param systemVersion
	 * @param deviceVersion
	 * @param deviceBrand
	 * @return
	 */
	@RequestMapping(value = "/reg", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<String> reg(String userTel, String pwd, 
			String verifyCode,Client clientType,String systemVersion,String deviceVersion,String deviceBrand){
		JsonResult<String> jr = null;
		try {
			jr = userService.reg(userTel, pwd, verifyCode, clientType, systemVersion, deviceVersion, deviceBrand);
		} catch(Exception e) {
			jr = new JsonResult<String>(false,"注册失败");
			log.info("注册userTel:{}",userTel,e);
		}
		
		return jr;
	}
	
	/**
	 * 登录
	 * @param userTel
	 * @param pwd
	 * @param clientType
	 *  @param verifyCode 验证码
	 * @param loginType 登录类型 01=密码 02=验证码
	 * @return
	 */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<Object> login(String userTel,String pwd,Client clientType,String verifyCode,String loginType){
    	JsonResult<Object> jr = null;
    	log.info("-------------------用户登录--------------");
    	log.info("userTel:{},clientType:{},verifyCode:{},loginType:{}",userTel,clientType,verifyCode,loginType);
    	try {
    		if(StringUtils.isEmpty(userTel)) {
    			return  new JsonResult<Object>(false,"手机号不能为空！");
    		}
    		if(StringUtils.isEmpty(clientType)) {
    			return  new JsonResult<Object>(false,"手机类型不能为空！");
    		}
    		if(StringUtils.isEmpty(loginType)) {
    			return  new JsonResult<Object>(false,"登录类型不能为空！");
    		}
    		if("01".equals(loginType)) {
    			if(StringUtils.isEmpty(pwd)) {
        			return  new JsonResult<Object>(false,"密码不能为空！");
        		} else {
        			pwd = Cipher.decryptCipher(pwd, clientType);
        		}
    		} else if("02".equals(loginType)) {
    			if(StringUtils.isEmpty(verifyCode)) {
        			return  new JsonResult<Object>(false,"验证码不能为空！");
        		}
    		} else {
    			return  new JsonResult<Object>(false,"登录类型错误！");
    		}
    		
    		jr = userService.login(userTel, pwd, clientType, verifyCode, loginType);
    	} catch(Exception e) {
    		jr = new JsonResult<Object>(false,"登录失败");
    		log.info("登录userTel:{}",userTel,e);
    		e.printStackTrace();
    	}
    	return jr;
    }
    
    /**
     * token刷新
     * @param request
     * @param userTel
     * @return
     */
    @RequestMapping(value = "/refreshToken", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult<Object> refreshToken(HttpServletRequest request,String userTel){
    	try {
    		if(StringUtils.isEmpty(userTel)) {
    			return  new JsonResult<Object>(false,"手机号不能为空！");
    		}
    		return userService.refreshToken(request);
    	} catch(Exception e) {
    		log.info("token刷新失败:{}",userTel,e);
    		return new JsonResult<Object>(false,"刷新失败！");
    		
    	}  	 
    }
    
    /**
     * 密码找回
     * @param userTel
     * @param pwd
     * @param verifyCode
     * @param clientType
     * @return
     */
    @RequestMapping(value = "/findPwd", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<String> findPwd(String userTel,String pwd,String verifyCode,Client clientType){
    	try {
    		if(StringUtils.isEmpty(userTel)) {
    			return  new JsonResult<String>(false,"手机号不能为空！");
    		}
    		if(StringUtils.isEmpty(pwd)) {
    			return  new JsonResult<String>(false,"密码不能为空！");
    		}
    		if(StringUtils.isEmpty(verifyCode)) {
    			return  new JsonResult<String>(false,"验证码不能为空！");
    		}
    		if(StringUtils.isEmpty(clientType)) {
    			return  new JsonResult<String>(false,"客户端类型不能为空！");
    		}
    		return userService.findPwd(userTel, pwd, verifyCode, clientType);
    	} catch(Exception e) {
    		log.info("userTel,密码修改:{}",userTel,e);
    		return new JsonResult<String>(false,"密码修改失败！");
    	}
    }
    
    
    /**
     * 短信发送
     * @param userTel
     * @param smsType
     * @return
     */
    @RequestMapping(value = "/sendSms", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<String> sms(String userTel,String smsType){
    	JsonResult<String> jr = null;
    	try {
    		log.info("验证码发送，userTel:{},smsType:{}",userTel,smsType);
    		jr = userService.sendSms(userTel, smsType);
    	} catch(Exception e) {
    		log.info("验证码发送:{}",userTel,e);
    		jr = new JsonResult<String>(false,"短信发送失败!");	
    	}
    	log.info("sendSmsJson"+JSON.toJSONString(jr));
		return jr;
    }
    
    /**
     * 短信校验
     * @param userTel
     * @param smsType
     * @param verifyCode
     * @return
     */
    @RequestMapping(value = "/checkSms", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<String> checkSmsVerifyCode(String userTel,String smsType,String verifyCode){
    	try {
    		return userService.checkSmsVerifyCode(userTel, smsType, verifyCode);
    	} catch(Exception e) {
    		log.info("userTel:{},smsType:{},verifyCode:{}",userTel,smsType,verifyCode,e);
    		return new JsonResult<String>(false,"网络异常，请稍后再试。");
    	}
    	
    }
    
    /**
     * 密码修改
     * @param userTel
     * @param oldPwd
     * @param newPwd
     * @param clientType
     * @return
     */
    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<String> updatePwd(String userTel,String oldPwd,String newPwd,Client clientType){
    	try {
    		if(StringUtils.isEmpty(userTel)) {
    			return  new JsonResult<String>(false,"手机号不能为空！");
    		}
    		if(StringUtils.isEmpty(oldPwd)) {
    			return  new JsonResult<String>(false,"原始密码不能为空！");
    		}
    		if(StringUtils.isEmpty(newPwd)) {
    			return  new JsonResult<String>(false,"新密码不能为空！");
    		}
    		if(StringUtils.isEmpty(clientType)) {
    			return  new JsonResult<String>(false,"客户端类型不能为空！");
    		}
    		return userService.updatePwd(userTel, oldPwd, newPwd, clientType);
    	} catch(Exception e) {
    		log.info("userTel,密码修改:{}",userTel,e);
    		return new JsonResult<String>(false,"密码修改失败！");
    	}
    }
    
    
    

}
