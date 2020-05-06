package com.lbd.lgame.auth.model.resp;

import java.io.Serializable;

public class CreditData<T> implements Serializable {

	private static final long serialVersionUID = -996205590417497055L;

	/**
	 *  响应码
	 *  60000 表示验证成功
	 *  其余以响应码表为准
	 */
	private String bizCode;
	
	/**
	 * 响应描述
	 * 响应码对应文字描述
	 */
	private String bizMsg;
	
	/**
	 * 返回对象
	 */
	private T data;
	
	public String getBizCode() {
		return bizCode;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	public String getBizMsg() {
		return bizMsg;
	}

	public void setBizMsg(String bizMsg) {
		this.bizMsg = bizMsg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "CreditData [bizCode=" + bizCode + ", bizMsg=" + bizMsg + ", data=" + data + "]";
	}	
}
