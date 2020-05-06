package com.lbd.lgame.portal.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lbd.lgame.common.api.CommonPage;
import com.lbd.lgame.common.utils.JsonResult;
import com.lbd.lgame.model.PubNotice;
import com.lbd.lgame.portal.service.HomeService;

/**
 * 首页内容展示
 * @author zhouhua
 *
 */
@Controller
@RequestMapping("/home")
public class HomeController {
	
	private static final Logger log = LoggerFactory.getLogger(HomeService.class);
	
	@Autowired
	private HomeService homeService;
	
	/**
	 * 分页查询测试
	 * @param pageSize 每页显示条数
	 * @param pageNum  当前第几页
	 * @return
	 */
	@RequestMapping(value = "/content", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult<Object> content(@RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum){
		try {
			List<PubNotice> pubNoticeList = homeService.getPubNoticeList(pageSize, pageNum);
			//分页查询数据返回说明  pubNoticeList=查询数据，CommonPage.restPage(pubNoticeList)=分页熟悉
			return new JsonResult<Object>(true,"查询成功").convtData(pubNoticeList,CommonPage.restPage(pubNoticeList));
		} catch(Exception e) {
			log.info("content:",e);
			return new JsonResult<Object>(false, "系统错误");
		}		
	}

}
