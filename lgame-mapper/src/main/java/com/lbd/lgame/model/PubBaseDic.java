package com.lbd.lgame.model;

import java.util.List;

public class PubBaseDic {
	//ID
	private String dicID; 
	//类型编号
	private String dicCode; 
	//字典类型
	private String dicType;
	//描述
	private String dicName;
	//上级字典ID
	private String parentDicCode;
	//状态，0-有效，1-无效。默认0
	private String status;
	//字典类型名称
	private String dicTypeName;
	//录入时间
	private String createTime;
	private List<PubBaseDic> pubBaseDics;
	private PubBaseDic pubBaseDic;
	//相关表表名
	private String sign;
	//创建人
	private String createUser;
	//修改时间
	private String editTime;
	//修改人
	private String editUser;
	
	public PubBaseDic getPubBaseDic() {
		return pubBaseDic;
	}
	public void setPubBaseDic(PubBaseDic pubBaseDic) {
		this.pubBaseDic = pubBaseDic;
	}
	public List<PubBaseDic> getPubBaseDics() {
		return pubBaseDics;
	}
	public void setPubBaseDics(List<PubBaseDic> pubBaseDics) {
		this.pubBaseDics = pubBaseDics;
	}
	public String getDicCode() {
		return dicCode;
	}
	public void setDicCode(String dicCode) {
		this.dicCode = dicCode;
	}
	public String getDicID() {
		return dicID;
	}
	public void setDicID(String dicID) {
		this.dicID = dicID;
	}
	public String getDicType() {
		return dicType;
	}
	public void setDicType(String dicType) {
		this.dicType = dicType;
	}
	public String getDicName() {
		return dicName;
	}
	public void setDicName(String dicName) {
		this.dicName = dicName;
	}
	public String getParentDicCode() {
		return parentDicCode;
	}
	public void setParentDicCode(String parentDicCode) {
		this.parentDicCode = parentDicCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDicTypeName() {
		return dicTypeName;
	}
	public void setDicTypeName(String dicTypeName) {
		this.dicTypeName = dicTypeName;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
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
	public void setEditUser(String editUser) {
		this.editUser = editUser;
	}
	
}