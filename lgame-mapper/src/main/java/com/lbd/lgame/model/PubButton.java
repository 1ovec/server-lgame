package com.lbd.lgame.model;

import java.io.Serializable;

public class PubButton implements Serializable{

	/**
	 * 开关
	 */
	private static final long serialVersionUID = 1L;
	
	//ID   
	private String id;
	// 开关类型,见字典表T0006
	private String btype;
	//1-打开,0-关闭
	private String status;
	//创建时间
	private String createTime;
	//创建人'
	private String createUser;
	//修改时间
	private String editTime;
	//修改人
	private String editUser;	
	//
	private String remark;
	// 字典编号
    private String dicCode;
    // 字典名称
    private String dicName;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBtype() {
		return btype;
	}
	public void setBtype(String btype) {
		this.btype = btype;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getEditTime() {
		return editTime;
	}
	public void setEditTime(String editTime) {
		this.editTime = editTime;
	}
	public String getEditUser() {
		return editUser;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDicCode() {
		return dicCode;
	}
	public void setDicCode(String dicCode) {
		this.dicCode = dicCode;
	}
	public String getDicName() {
		return dicName;
	}
	public void setDicName(String dicName) {
		this.dicName = dicName;
	}
	public void setEditUser(String editUser) {
		this.editUser = editUser;
	}
	
	
}
