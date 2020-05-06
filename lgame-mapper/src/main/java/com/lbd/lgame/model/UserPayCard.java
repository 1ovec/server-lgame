package com.lbd.lgame.model;

/**
 * @author zhouhua
 * 2020-01-15
 * I am not responsible for this code。
 */
public class UserPayCard {
	
	private String userID;
	private String cardNo; //卡号
	private String bankName; //银行卡名称
	private String cardID; //银行卡模板ID
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getCardID() {
		return cardID;
	}
	public void setCardID(String cardID) {
		this.cardID = cardID;
	}
	
	

}
