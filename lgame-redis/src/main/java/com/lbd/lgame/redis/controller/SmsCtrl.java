package com.lbd.lgame.redis.controller;

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
import com.lbd.lgame.redis.service.SmsService;


@Controller
@RequestMapping(value="/checkSms")
public class SmsCtrl {	
	private static final Logger log = LoggerFactory.getLogger(SmsCtrl.class);
	
	@Autowired
	private SmsService smsService;
	
	/**
	 * 检验手机号码短信发送次数
	 * @param phone
	 * @param smsTypem
	 * @param TempletTime
	 * @param verifyCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/verifyPhoneTimes" ,method=RequestMethod.POST)
	public JsonResult<String> verifyPhoneTimes(@RequestParam("phone") String phone) {		
		log.info("----------进入校验短信发送次数----------------");
		log.info("手机号码：{}",phone);
		//校验短信发送次数参数
		if(StringUtils.isEmpty(phone)){
			   return new JsonResult<String>(false,"非法请求参数");
		}
		//返回结果
		JsonResult<String> jr = null;
		try {					
			jr = smsService.verifyPhoneTimes(phone);	
		} catch (Exception e) {
			log.error(e.toString());
			log.error(e.getMessage());
			jr = new JsonResult<String>(false,"系统错误，请联系管理员");
		}
		return jr;
	}	
	
	/**
	 * 设置验证码超时时间 累计短信发送次数
	 * @param phone
	 * @param smsType
	 * @param templetTime
	 * @param verifyCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/smsIncr",method=RequestMethod.POST)
	public JsonResult<String> smsIncr(@RequestParam("phone") String phone,@RequestParam("smsType") String smsType,@RequestParam("templetTime") Integer templetTime,@RequestParam("verifyCode") String verifyCode) {
		log.info("----------进入手机号码短信累计发送次数----------------");
		log.info("手机号码：{}", phone);
		log.info("短信类型：{}", smsType);
		log.info("验证码过期时间：{}", templetTime);
		log.info("验证码：{}", verifyCode);
		// 校验短信发送次数参数
		if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(smsType) || StringUtils.isEmpty(templetTime)
				|| StringUtils.isEmpty(verifyCode)) {
			return new JsonResult<String>(false, "非法请求参数");
		}
		// 返回结果
		JsonResult<String> jr = null;
		try {
			jr = smsService.smsIncr(phone, smsType, templetTime, verifyCode);
		} catch (Exception e) {
			log.error(e.toString());
			log.error(e.getMessage());
			jr = new JsonResult<String>(false, "系统错误，请联系管理员");
		}
		return jr;
	}

	/**
	 * 校验短信验证码
	 * 
	 * @param phone
	 * @param smsType
	 * @param verifyCode
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkSmsVerifyCode", method = RequestMethod.POST)
	public JsonResult<String> checkSmsVerifyCode(@RequestParam("phone") String phone,@RequestParam("smsType") String smsType,@RequestParam("verifyCode") String verifyCode) {
		log.info("----------进入校验短信验证码----------------");
		log.info("手机号码：{}", phone);
		log.info("短信类型：{}", smsType);
		log.info("验证码：{}", verifyCode);
		// 校验短信发送次数参数
		if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(smsType) || StringUtils.isEmpty(verifyCode)) {
			return new JsonResult<String>(false, "非法请求参数");
		}
		// 返回结果
		JsonResult<String> jr = null;
		try {
			jr = smsService.checkSmsVerifyCode(phone, verifyCode, smsType);
		} catch (Exception e) {
			log.error(e.toString());
			log.error(e.getMessage());
			jr = new JsonResult<String>(false, "系统错误，请联系管理员");
		}
		return jr;
	}

}
