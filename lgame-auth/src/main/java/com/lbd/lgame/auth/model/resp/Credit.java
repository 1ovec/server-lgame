package com.lbd.lgame.auth.model.resp;


public class Credit {
	
	//卡号
	private String idcard;
	//银行名称
	private String bankName;
	private String sysnb;
	//姓名
	private String name;
	private String expireDate;
	//流水id
	private String recordId;
	private String token;
	private String cvn2;
	//手机号
	private String phoneno;
	//身份证号码
	private String acctno;
	
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getSysnb() {
		return sysnb;
	}
	public void setSysnb(String sysnb) {
		this.sysnb = sysnb;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCvn2() {
		return cvn2;
	}
	public void setCvn2(String cvn2) {
		this.cvn2 = cvn2;
	}
	public String getPhoneno() {
		return phoneno;
	}
	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}
	public String getAcctno() {
		return acctno;
	}
	public void setAcctno(String acctno) {
		this.acctno = acctno;
	}
	
	@Override
	public String toString() {
		return "Credit [idcard=" + idcard + ", bankName=" + bankName + ", sysnb=" + sysnb + ", name=" + name
				+ ", expireDate=" + expireDate + ", recordId=" + recordId + ", token=" + token + ", cvn2=" + cvn2
				+ ", phoneno=" + phoneno + ", acctno=" + acctno + "]";
	}	
}