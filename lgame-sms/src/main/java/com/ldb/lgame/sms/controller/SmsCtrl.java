package com.ldb.lgame.sms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lbd.lgame.common.utils.JsonResult;
import com.ldb.lgame.sms.service.SmsService;


@Controller
@RequestMapping(value="/sms")
public class SmsCtrl {
	private static final Logger log = LoggerFactory.getLogger(SmsCtrl.class);
	@Autowired
	private SmsService smsService;
	
	
	
	@ResponseBody
	@RequestMapping(value="/sendSms", method = RequestMethod.POST)
	public JsonResult<String> sendSms(@RequestParam("phoneNo") String phoneNo, @RequestParam("smsType") String smsType) {		
		log.info("----------进入发送短信接口开始-----------------");
		log.info("手机号码：{}",phoneNo);
		log.info("短信类型：{}",smsType);	
		//校验发送短信参数
		if(StringUtils.isEmpty(phoneNo) || StringUtils.isEmpty(smsType)){
			return new JsonResult<String>(false, "非法请求参数");
		}
		//返回结果
		JsonResult<String> jr = null;
		try {					
			jr = smsService.sendSms(phoneNo, smsType);		
		} catch (Exception e) {
			log.error(e.toString());
			log.error(e.getMessage());
			jr = new JsonResult<String>(false,"系统错误，请联系管理员");
		}
		return jr;
	}		
}
