package com.lbd.lgame.wish.controller;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.lbd.lgame.common.utils.JsonResult;
import com.lbd.lgame.wish.service.WishService;

/**
 * @author zhouhua
 * 2020-01-06
 * I am not responsible for this code。
 */
@Controller
@RequestMapping("/wish")
public class WishCtrl {
	
	private static final Logger log = LoggerFactory.getLogger(WishCtrl.class);
	
	@Autowired
	private WishService wishService;
	
	@RequestMapping("/test")
	@ResponseBody
	public JsonResult<String> test(){
		 try {
			 String result = wishService.test();
			 return new JsonResult<String>(true, result);
		 } catch(Exception e) {
			 log.info("wish-test",e);
			 return new  JsonResult<String>(false, "测试失败");
		 }
	}

	/*
	 * 用户心愿数据新增
	 * @Author YUAN
	 * */
	@ResponseBody
	@RequestMapping(value="/userWish", method = RequestMethod.POST)
	public JsonResult<String> saveUserWish(@RequestParam("userId") String userId,@RequestParam("userTel") String userTel, @RequestParam("wishName") String wishName, @RequestParam("wishText") String wishText, @RequestParam("wishPath") String wishPath, @RequestParam("price") String price) {
		log.info("----------进入心愿接口开始-----------------");
		log.info("用户ID：{}",userId);
		log.info("用户手机号：{}",userTel);
		log.info("心愿标题：{}",wishName);
		log.info("心愿内容：{}",wishText);
		log.info("图片路径：{}",wishPath);
		log.info("价格：{}",price);
		//校验发送参数
		if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(wishName) || StringUtils.isEmpty(userTel) || StringUtils.isEmpty(wishText)|| StringUtils.isEmpty(price)){
			return new JsonResult<String>(false, "请求参数数据为空");
		}
		//返回结果
		JsonResult<String> jr = null;
		try {
			jr = wishService.saveUserWish(userId,wishName,wishText,wishPath,price);
			log.info("用户心愿数据新增 + jr : {}", JSON.toJSONString(jr));
		} catch (Exception e) {
			log.info("系统错误,新增异常:{}",e);
			jr = new JsonResult<String>(false,"系统繁忙");
		}
		return jr;
	}

}
