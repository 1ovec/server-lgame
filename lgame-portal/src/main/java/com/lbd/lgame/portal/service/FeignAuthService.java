package com.lbd.lgame.portal.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.lbd.lgame.common.utils.JsonResult;

/**
 * @author zhouhua
 * 2020-01-13
 * I am not responsible for this code。
 */
@FeignClient(value = "LGAME-AUTH",  path="/lgame-auth")
public interface FeignAuthService {
	
	/**
	 * 实名认证二要素
	 * @param userId
	 * @param cId
	 * @param userName
	 * @return
	 */
	@PostMapping("/userRealAuth/verifyIcCard")
	JsonResult<String> verifyIcCard(@RequestParam("userId")String userId,@RequestParam("cId")String cId,@RequestParam("userName")String userName);
	
	
	/**
	 * 实名认证四要素
	 * @param userId
	 * @param cId
	 * @param cardNo
	 * @param realTel
	 * @param userName
	 * @return
	 */
	@PostMapping("/userRealAuth/bankVerifyFour")
	 JsonResult<String> bankVerifyFour(@RequestParam("userId")String userId,@RequestParam("cId")String cId,@RequestParam("cardNo")String cardNo,@RequestParam("realTel")String realTel,@RequestParam("userName")String userName);

}
