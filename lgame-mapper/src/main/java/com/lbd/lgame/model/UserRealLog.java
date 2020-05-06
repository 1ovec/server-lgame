package com.lbd.lgame.model;

import java.io.Serializable;

public class UserRealLog implements Serializable{

	/**
	 * 实名认证历史信息表
	 */
	private static final long serialVersionUID = 1L;
	//ID   
	private String id;
	//用户ID'
	private String userId;
	//创建时间'
	private String createTime;
	//状态,1-未认证,2-认证成功,3-认证中,4-认证失败
	private String status;
	//备注
	private String remark;
	//姓名
	private String userName;
	//身份证号码
	private String cId;
	//卡号
	private String cardNo;
	//预留手机号
	private String realTel;
	//认证编号
	private String realCode;
	//认证时间
	private String realTime;
	//失败原因
	private String errMsg;
	//实名认证类型：1.银行卡四要素认证 2.银行卡两要素认证 3.身份证实名认证
	private String realAuthType;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getRealCode() {
		return realCode;
	}
	public void setRealCode(String realCode) {
		this.realCode = realCode;
	}	
	public String getRealTime() {
		return realTime;
	}
	public void setRealTime(String realTime) {
		this.realTime = realTime;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public String getRealAuthType() {
		return realAuthType;
	}
	public void setRealAuthType(String realAuthType) {
		this.realAuthType = realAuthType;
	}	
}
