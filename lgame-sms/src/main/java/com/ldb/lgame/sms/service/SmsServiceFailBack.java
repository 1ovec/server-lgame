package com.ldb.lgame.sms.service;

import org.springframework.stereotype.Component;

import com.lbd.lgame.common.utils.JsonResult;

@Component
public class SmsServiceFailBack extends SmsService{
	
	@Override
	public JsonResult<String> sendSms(String phoneNo,String smsType)throws Exception {
		return new JsonResult<String>(false,"短信发送失败！");
	}

}
