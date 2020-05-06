package com.lbd.lgame.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lbd.lgame.common.utils.JsonResult;
import com.lbd.lgame.portal.service.TestService;

@Controller
public class TestCtrl {
	
	@Autowired
	private TestService testService;
	
	@ResponseBody
	@RequestMapping("test")
	public JsonResult<String> test(){
		JsonResult<String> jr = new JsonResult<String>(true,"这是测试");
		return jr;
	}
	
	
	@ResponseBody
	@RequestMapping("testSQL")
	public JsonResult<String> testSQL(){
		int count = testService.testSQL();
		JsonResult<String> jr = new JsonResult<String>(true,"这是测试  "+count);
		return jr;
	}
	
	@ResponseBody
	@RequestMapping("testInfo")
	public JsonResult<String> testInfo(){
		String count = testService.testInfo();
		JsonResult<String> jr = new JsonResult<String>(true,"这是测试  "+count);
		return jr;
	}
	
	

}
