package com.ldb.lgame.sms.model.resp;


public class SendSms {
	
	private String detail;
	//手机号码
	private String phone;
	//流水号
	private String waterId;
	private String code;
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getWaterId() {
		return waterId;
	}
	public void setWaterId(String waterId) {
		this.waterId = waterId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}		
}