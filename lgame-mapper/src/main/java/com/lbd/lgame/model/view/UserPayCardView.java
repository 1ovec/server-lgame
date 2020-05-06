package com.lbd.lgame.model.view;

import com.lbd.lgame.model.UserPayCard;

/**
 * @author zhouhua
 * 2020-01-15
 * I am not responsible for this code。
 */
public class UserPayCardView extends UserPayCard{
	
	private String userName;
	private String cardType; //银行卡类型1-储蓄卡,2-信用卡
	private String iconPath; //icon地址
	
	

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	

}
