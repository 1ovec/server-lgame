package com.lbd.lgame.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.lbd.lgame.auth.service.UserRealAuthService;
import com.lbd.lgame.common.utils.JsonResult;
import com.lbd.lgame.common.utils.StringUtils;

@Controller
@RequestMapping(value = "/userRealAuth")
public class UserRealAuthCtrl {
	private static final Logger log = LoggerFactory.getLogger(UserRealAuthCtrl.class);
	@Autowired
	private UserRealAuthService userRealAuthService;

	/**
	 * 银行卡四要素认证
	 * 
	 * @param user
	 * @param cId
	 * @param cardNo
	 * @param realTel
	 * @param userName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/bankVerifyFour", method = RequestMethod.POST)
	public JsonResult<String> bankVerifyFour(@RequestParam("userId") String userId, @RequestParam("cId") String cId,
			@RequestParam("cardNo") String cardNo, @RequestParam("realTel") String realTel,
			@RequestParam("userName") String userName) {
		log.info("----------进入银行卡四要素认证接口开始-----------------");
		log.info("userId:{}", userId);
		log.info("银行卡四要素认证姓名：{}", userName);
		log.info("身份证号码：{}", cId);
		log.info("银行卡四要素认证手机号：{}", realTel);
		log.info("银行卡四要素认证卡号：{}", cardNo);
		// 校验四要素认证参数
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(cId) || StringUtils.isBlank(cardNo)
				|| StringUtils.isBlank(realTel) || StringUtils.isBlank(userName)) {
			return new JsonResult<String>(false, "非法请求参数");
		}
		// 返回结果
		JsonResult<String> jr = null;
		try {
			jr = userRealAuthService.bankVerifyFour(userId, cId, cardNo, realTel, userName);
		} catch (Exception e) {
			log.error(e.toString());
			log.error(e.getMessage());
			jr = new JsonResult<String>(false, "系统错误，请联系管理员");
		}
		return jr;
	}

	/**
	 * 实名认证
	 * 
	 * @param userId
	 * @param cId
	 * @param userName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/verifyIcCard", method = RequestMethod.POST)
	public JsonResult<String> verifyIcCard(@RequestParam("userId") String userId, @RequestParam("cId") String cId,
			@RequestParam("userName") String userName) {
		log.info("----------进入用户实名认证接口开始-----------------");
		log.info("userId:{}", userId);
		log.info("实名认证姓名：{}", userName);
		log.info("身份证号码：{}", cId);
		// 返回结果
		JsonResult<String> jr = null;

		try {
			// 校验实名认证参数
			if (StringUtils.isBlank(userId) || StringUtils.isBlank(cId) || StringUtils.isBlank(userName)) {
				return new JsonResult<String>(false, "系统繁忙请稍后再试-08");
			}
			jr = userRealAuthService.verifyIcCard(userId, cId, userName);
		} catch (Exception e) {
			log.error("实名认证错误，userId:{}", userId, e);
			jr = new JsonResult<String>(false, "系统繁忙请稍后再试-07");
		}
		return jr;
	}

	/**
	 * 银行卡两要素认证
	 * 
	 * @param userId
	 * @param cardNo
	 * @param userName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/verifyBankCard", method = RequestMethod.POST)
	public JsonResult<String> verifyBankCard(@RequestParam("userId") String userId,
			@RequestParam("cardNo") String cardNo, @RequestParam("userName") String userName) {
		log.info("----------进入银行卡两要素认证接口开始-----------------");
		log.info("userId:{}", userId);
		log.info("银行卡两要素认证姓名：{}", userName);
		log.info("银行卡两要素认证卡号：{}", cardNo);
		// 校验银行卡两要素认证参数
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(cardNo) || StringUtils.isBlank(userName)) {
			return new JsonResult<String>(false, "非法请求参数");
		}
		// 返回结果
		JsonResult<String> jr = null;
		try {
			jr = userRealAuthService.verifyBankCard(userId, cardNo, userName);
		} catch (Exception e) {
			log.error(e.toString());
			log.error(e.getMessage());
			jr = new JsonResult<String>(false, "系统错误，请联系管理员");
		}
		return jr;
	}
}
