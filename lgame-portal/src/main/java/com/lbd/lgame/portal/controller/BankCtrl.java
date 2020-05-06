package com.lbd.lgame.portal.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lbd.lgame.common.api.CommonPage;
import com.lbd.lgame.common.utils.JsonResult;
import com.lbd.lgame.common.utils.Utils;
import com.lbd.lgame.model.LgCardList;
import com.lbd.lgame.model.UserPayCard;
import com.lbd.lgame.model.view.UserPayCardView;
import com.lbd.lgame.portal.service.BankService;

/**
 * @author zhouhua
 * 2020-01-14
 * I am not responsible for this code。
 */
@Controller
@RequestMapping(value="/bank")
public class BankCtrl {
	
	private static final Logger log = LoggerFactory.getLogger(BankService.class);
	
	@Autowired
	private BankService bankService;
	
	/**
	 * 获取银行卡列表
	 * @param pageSize
	 * @param pageNum
	 * @param cardType
	 * @return
	 */
	@RequestMapping("/getBackList")
	@ResponseBody
	public JsonResult<Object> getBackList(@RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,String cardType){
		log.info("----获取银行卡列表----");
		try {
			if(StringUtils.isEmpty(cardType)) {
				return new JsonResult<Object>(false,"系统繁复请稍后再试-02");
			}
			List<LgCardList> lgCardList = bankService.queryCardList(pageSize, pageNum, cardType);
			log.info("lgCardList:",lgCardList.size());
			return new JsonResult<Object>(true,"查询成功").convtData(lgCardList,CommonPage.restPage(lgCardList));
		} catch(Exception e) {
			log.error("获取银行卡列表",e);
			return new JsonResult<Object>(false,"系统繁复请稍后再试-01");
		}
	}
	
	@RequestMapping("/bindBank")
	@ResponseBody
	public JsonResult<String> bindBank(UserPayCard userPayCard){
		log.info("----用户绑卡接口----");
		try {
			String userID = userPayCard.getUserID();
		log.info("userID:",userID);
		
			
		} catch(Exception e) {
			log.error("用户绑卡失败，userID:",userPayCard.getUserID());
		}
		return null;
	}
	
	/**
	 * 获取用户绑卡信息
	 * @param userID
	 * @return
	 */
	@RequestMapping("/getPayCard")
	@ResponseBody
	public JsonResult<Object> getPayCard(String userID){
		log.info("----进入用户银行卡页面---- ");
		log.info("userID:{}",userID);
		try {
			if(StringUtils.isEmpty(userID)) {
				return new JsonResult<Object>(false,"系统繁忙，请稍后再试-01");
			}
			UserPayCardView userPayCardView =bankService.getUserPayCardInfo(userID);
			return new JsonResult<Object>(true,"查询成功").convtData(userPayCardView);
		} catch(Exception e) {
			log.error("获取用户卡失败，userID:{}",userID,e);
			return new JsonResult<Object>(false,"查询失败").convtData(null);
		}
	}
	

}
