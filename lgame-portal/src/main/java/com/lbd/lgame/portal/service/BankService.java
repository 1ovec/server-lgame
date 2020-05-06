package com.lbd.lgame.portal.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.lbd.lgame.common.utils.StringUtils;
import com.lbd.lgame.common.utils.Utils;
import com.lbd.lgame.commons.encryption.DESCoder;
import com.lbd.lgame.mapper.LgCardListMapper;
import com.lbd.lgame.mapper.UserPayCardMapper;
import com.lbd.lgame.model.LgCardList;
import com.lbd.lgame.model.view.UserPayCardView;

/**
 * @author zhouhua
 * 2020-01-14
 * I am not responsible for this code。
 */
@Service
public class BankService {
	
	private static final Logger log = LoggerFactory.getLogger(BankService.class);
	
	@Autowired
	private LgCardListMapper lgCardListMapper;
	
	@Autowired
	private UserPayCardMapper userPayCardMapper;
	
	/**
	 * 获取银行卡列表
	 * @param pageSize
	 * @param pageNum
	 * @param cardType
	 * @return
	 */
	public List<LgCardList> queryCardList(Integer pageSize, Integer pageNum,String cardType){
		PageHelper.startPage(pageNum,pageSize);
		List<LgCardList> lgCardList = lgCardListMapper.queryCardList(cardType);
		return lgCardList;		
	}
	
	/**
	 * 查询用户是否具有有效绑卡记录
	 * @param userID
	 * @return
	 */
	public int queryUserPayCardCount(String userID) {
		return userPayCardMapper.queryUserPayCardCount(userID);
	}
	
	/**
	 * 查询用户绑卡信息
	 * @param userID
	 * @return
	 * @throws ClassNotFoundException 
	 */
	public UserPayCardView getUserPayCardInfo(String userID) throws ClassNotFoundException {
		UserPayCardView userPayCardView = userPayCardMapper.getUserPayCardInfo(userID);
		if(Utils.isEmpty(userPayCardView)) {
			return null;
		}
		
		String cardNo = userPayCardView.getCardNo();
		cardNo = DESCoder.getInstance().dencrypt(cardNo);
		cardNo = StringUtils.bankCardNoMask(cardNo);
		if(StringUtils.isEmpty(cardNo)) {
			return null;
		}
		log.info("卡号脱敏，userID:{},cardNo:{}",userID,cardNo);
		userPayCardView.setCardNo(cardNo);
		return userPayCardView;
	}

}
