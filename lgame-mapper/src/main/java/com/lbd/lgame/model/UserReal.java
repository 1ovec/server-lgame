package com.lbd.lgame.model;

import java.io.Serializable;

public class UserReal implements Serializable{

	/**
	 * APP用户认证信息实体类
	 */
	private static final long serialVersionUID = 1L;
	//ID 
	private String userRealId;
	//用户ID 
	private String userId;
	//1-未认证,2-认证成功,3-认证中,4-认证失败
	private String realStatus;
	//认证时间
	private String realTime;
	//备注
	private String remark;
	//用户姓名
	private String userName;
	//用户身份证号码
	private String cId;
	//实名认证卡号
	private String cardNo;
	//实名认证手机号
	private String realTel;
	//创建时间
	private String createTime;
	//失败原因
	private String errMsg;
	
	public String getUserRealId() {
		return userRealId;
	}
	public void setUserRealId(String userRealId) {
		this.userRealId = userRealId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRealStatus() {
		return realStatus;
	}
	public void setRealStatus(String realStatus) {
		this.realStatus = realStatus;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getcId() {
		return cId;
	}
	public void setcId(String cId) {
		this.cId = cId;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getRealTel() {
		return realTel;
	}
	public void setRealTel(String realTel) {
		this.realTel = realTel;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public String getRealTime() {
		return realTime;
	}
	public void setRealTime(String realTime) {
		this.realTime = realTime;
	}	
}
