package com.lbd.lgame.model;

/**
 * @author zhouhua
 * 2020-01-14
 * I am not responsible for this code。
 */
public class LgCardList {
	
	private String bankName; //银行名称
	private String cardType; //银行卡类型1-储蓄卡,2-信用卡
	private String iconPath; //icon地址
	private String firstLetter; //首字母
	

	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
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
	public String getFirstLetter() {
		return firstLetter;
	}
	public void setFirstLetter(String firstLetter) {
		this.firstLetter = firstLetter;
	}
	
	

}
