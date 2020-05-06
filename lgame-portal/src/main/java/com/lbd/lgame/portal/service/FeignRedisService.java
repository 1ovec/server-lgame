package com.lbd.lgame.portal.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lbd.lgame.common.utils.JsonResult;

/**
 * Redis
 * @author zhouhua
 * 2020-01-06
 * I am not responsible for this code。
 */
@FeignClient("lgame-redis")
public interface FeignRedisService {
	
	
	/**
	 * 短信校验
	 * @param phone
	 * @param smsType
	 * @param verifyCode
	 * @return
	 */
	@PostMapping("/checkSms/checkSmsVerifyCode")
	JsonResult<String> checkSmsVerifyCode(@RequestParam("phone") String phone,@RequestParam("smsType") String smsType,@RequestParam("verifyCode") String verifyCode);
		
	

}
