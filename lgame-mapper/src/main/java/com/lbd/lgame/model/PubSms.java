package com.lbd.lgame.model;

import java.io.Serializable;

public class PubSms implements Serializable{
	/**
	 * 短信实体类
	 */
	private static final long serialVersionUID = 7320767535877328109L;

	// ID
	private String id;
	// 手机号
	private String iphone;
	// 验证码
	private String verifyCode;
	// 类型
	private String type;
	// 正文
	private String content;
	// 创建时间
	private String createTime;
	// 返回时间
	private String resultTime;
	// 状态,1-未发送,2-已发送,3-验证成功,4-验证失败'
	private String status;
	// 返回状态,1未返回,2-返回成功,3-返回失败
	private String resultStatus;
	// 返回内容
	private String resultTxt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIphone() {
		return iphone;
	}

	public void setIphone(String iphone) {
		this.iphone = iphone;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getResultTime() {
		return resultTime;
	}

	public void setResultTime(String resultTime) {
		this.resultTime = resultTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}

	public String getResultTxt() {
		return resultTxt;
	}
	
	public void setResultTxt(String resultTxt) {
		this.resultTxt = resultTxt;
	}
}
