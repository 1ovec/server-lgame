package com.ldb.lgame.sms.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.lbd.lgame.common.utils.JsonResult;


/**
 * 调用redis程序接口
 *
 */
@FeignClient(value="LGAME-REDIS", path="/lgame-redis", fallback=SmsServiceFailBack.class)
public interface FeignRedisService {
	
	/**
	 * 校验短信验证码发送次数
	 * @param phone
	 * @return
	 */
	@PostMapping("/checkSms/verifyPhoneTimes")
	JsonResult<String> verifyPhoneTimes(@RequestParam("phone") String phone);
	
	
	/**
	 * 设置验证码超时时间 累计短信发送次数
	 * @param phone
	 * @param smsType
	 * @param templetTime
	 * @param verifyCode
	 * @return
	 */
	@PostMapping("/checkSms/smsIncr")
	JsonResult<String> smsIncr(@RequestParam("phone") String phone,@RequestParam("smsType") String smsType,@RequestParam("templetTime") Integer templetTime,@RequestParam("verifyCode") String verifyCode);
}