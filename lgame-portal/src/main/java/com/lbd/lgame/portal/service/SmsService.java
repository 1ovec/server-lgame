package com.lbd.lgame.portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lbd.lgame.common.utils.JsonResult;

/**
 * @author zhouhua
 * 2020-01-06
 * I am not responsible for this code。
 */
@Service
public class SmsService {
	
	@Autowired
	private FeignRedisService feignRedisService;
	
	/**
	 * 短信校验
	 * @param phone
	 * @param smsType
	 * @param verifyCode
	 * @return
	 */
	public JsonResult<String> checkSmsVerifyCode(String phone, String smsType, String verifyCode){
		return feignRedisService.checkSmsVerifyCode(phone, smsType, verifyCode);
	}

}
