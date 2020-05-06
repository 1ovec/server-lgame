package com.lbd.lgame.model;

public class User {
	
	/**
	 * app用户实体类
	 */	
	//主键ID
	private String userId;
	//邮箱
	private String userMail;
	//密码
	private String userPasswd;
	//电话
	private String userTel;
	//实名认证ID
	private String userRealId;
	//注册用户状态.01-正常;02-冻结.默认01
	private String status;
	//注册时间.默认系统时间
	private String regTime;
	//令牌
	private String token;
	//当前app的版本号
	private String appVesionCode;
	//备注字段
	private String remark;
	//用户最后一次登录时间
	private String lastLoginTime;
	//登录设备类型.Android-安卓.IOS-01苹果商店版本,IOS-02苹果企业版
	private String loginEquipMent;
	//当天登录失败次数
	private String loginFailCount;
	//登录错误日期,YYYYMMDD格式
	private String loginFailDate;
	//手机型号
	private String deviceVersion;
	//系统版本号
	private String systemVersion;
	//手机厂商
	private String deviceBrand;
	//昵称
	private String nickName;
	//用户头像地址
	private String imagePath;
	//实名认证状态
	private String realStatus;
	
	private String tokenHead;;	
	//用户姓名
	private String userName;
	
	public String getTokenHead() {
		return tokenHead;
	}
	public void setTokenHead(String tokenHead) {
		this.tokenHead = tokenHead;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserMail() {
		return userMail;
	}
	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}
	public String getUserPasswd() {
		return userPasswd;
	}
	public void setUserPasswd(String userPasswd) {
		this.userPasswd = userPasswd;
	}
	public String getUserTel() {
		return userTel;
	}
	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}
	public String getUserRealId() {
		return userRealId;
	}
	public void setUserRealId(String userRealId) {
		this.userRealId = userRealId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRegTime() {
		return regTime;
	}
	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	public String getAppVesionCode() {
		return appVesionCode;
	}
	public void setAppVesionCode(String appVesionCode) {
		this.appVesionCode = appVesionCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getLoginEquipMent() {
		return loginEquipMent;
	}
	public void setLoginEquipMent(String loginEquipMent) {
		this.loginEquipMent = loginEquipMent;
	}
	public String getLoginFailCount() {
		return loginFailCount;
	}
	public void setLoginFailCount(String loginFailCount) {
		this.loginFailCount = loginFailCount;
	}
	public String getLoginFailDate() {
		return loginFailDate;
	}
	public void setLoginFailDate(String loginFailDate) {
		this.loginFailDate = loginFailDate;
	}
	public String getDeviceVersion() {
		return deviceVersion;
	}
	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
	}
	public String getSystemVersion() {
		return systemVersion;
	}
	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}
	public String getDeviceBrand() {
		return deviceBrand;
	}
	public void setDeviceBrand(String deviceBrand) {
		this.deviceBrand = deviceBrand;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getRealStatus() {
		return realStatus;
	}
	public void setRealStatus(String realStatus) {
		this.realStatus = realStatus;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
