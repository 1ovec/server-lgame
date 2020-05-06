package com.ldb.lgame.sms.model.resp;

import java.io.Serializable;

public class SendSmsRespData<T> implements Serializable {

	private static final long serialVersionUID = 5210484858676908422L;

	/**
	 *  错误码code
	 *  10000 表示网关处理成功,并正常返回
	 *  其余响应码以响应业务错误码为准
	 */
	private String code;
	
	/**
	 * 响应描述
	 * 响应码对应文字描述
	 */
	private String msg;
	

	/**
	 * 返回对象
	 */
	private T responseData;
	

	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}


	public T getResponseData() {
		return responseData;
	}


	public void setResponseData(T responseData) {
		this.responseData = responseData;
	}


	@Override
	public String toString() {
		return "RespData [code=" + code + ", msg=" + msg + ", responseData=" + responseData + "]";
	}
		
}