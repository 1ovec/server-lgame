package com.lbd.lgame.portal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lbd.lgame.common.utils.JsonResult;
import com.lbd.lgame.common.utils.StringUtils;
import com.lbd.lgame.portal.service.AuthService;

/**
 * @author zhouhua
 * 2020-01-13
 * I am not responsible for this code。
 */
@Controller
@RequestMapping(value="/userAuth")
public class AuthCtrl {
	
	private static final Logger log = LoggerFactory.getLogger(AuthCtrl.class);	
	
	@Autowired
	private AuthService authService;
	
	/**
	 * 实名认证二要素
	 * @param userTel
	 * @param userId
	 * @param cId
	 * @param userName
	 * @return
	 */
	@RequestMapping("verifyIcCard")
	@ResponseBody
	public JsonResult<String> verifyIcCard(String userTel,String userId,String cID,String userName,String imagePath1,String imagePath2){
		log.info("----实名认证----");
		log.info("userTel:{},userName:{}",userTel,userName);
		try {//校验实名认证参数
			if(StringUtils.isBlank(userTel) || StringUtils.isBlank(userId) ){
				 return new JsonResult<String>(false,"系统繁忙请稍后再试-08");
			}
			if(StringUtils.isBlank(cID)) {
				 return new JsonResult<String>(false,"身份证号码不能为空！");
			}
			if(StringUtils.isBlank(userName)) {
				 return new JsonResult<String>(false,"姓名不能为空！");
			}
			if(StringUtils.isEmpty(imagePath1)) {
				 return new JsonResult<String>(false,"身份证正面照不能为空！");
			}
			if(StringUtils.isEmpty(imagePath2)) {
				 return new JsonResult<String>(false,"身份证反面照不能为空！");
			}
			return authService.verifyIcCard(userTel, userId, cID, userName);
		} catch(Exception e) {
			log.error("实名认证userTel:{},userName:{}",userTel,userName,e);
			return new JsonResult<String>(false,"系统繁忙，请稍后再试-07!");
		}
	}

}
