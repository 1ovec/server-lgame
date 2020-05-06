package com.lbd.lgame.model;

import java.io.Serializable;

/**
 * @author Administrator
 *
 */
public class PubSmsSet implements Serializable{

	/**
	 * 短信模板配置
	 */
	private static final long serialVersionUID = -1352580127862868403L;
		
	// ID
	private String id;
	//短信模板
	private String smsTemplate;
	//短信类型,字典表T0008
	private String smsType;
	//0-无效,1-有效
	private String status;
	//创建时间
	private String createTime;
	//创建人
	private String createUser;
	//修改时间
	private String editTime;
	// 修改人
	private String editUser;
	//备注
	private String remark;
	//有效时间(分钟)
	private Integer expTime;
	//短信模板正文
	private String smsTemplateContent; 
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSmsTemplate() {
		return smsTemplate;
	}
	public void setSmsTemplate(String smsTemplate) {
		this.smsTemplate = smsTemplate;
	}
	public String getSmsType() {
		return smsType;
	}
	public void setSmsType(String smsType) {
		this.smsType = smsType;
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
	public void setEditUser(String editUser) {
		this.editUser = editUser;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getExpTime() {
		return expTime;
	}
	public void setExpTime(Integer expTime) {
		this.expTime = expTime;
	}
	public String getSmsTemplateContent() {
		return smsTemplateContent;
	}
	public void setSmsTemplateContent(String smsTemplateContent) {
		this.smsTemplateContent = smsTemplateContent;
	}		
}
