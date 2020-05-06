package com.lbd.lgame.portal.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lbd.lgame.common.utils.JsonResult;

/**
 * zhouhua
 * 2020-01-04
 * I am not responsible for this code。
 * 调用短信程序
 * @FeignClient("lgame-sms")为项目在服务中心的注册名
 * @PostMapping("/sms/sendSms") 方法名
 */
@FeignClient(value="LGAME-SMS", path="/lgame-sms")
public interface FeignSmsService {
	
	/**
	 * 短信发送
	 * @param phoneNo
	 * @param smsType
	 * @return
	 */
	@PostMapping("/sms/sendSms")
	JsonResult<String> sendSms(@RequestParam("phoneNo") String phoneNo,@RequestParam("smsType")String smsType);
	
	
	

}
