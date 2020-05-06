package com.lbd.lgame.common.constant;

import java.util.ResourceBundle;

public class TencentCosParam {

	/**
	 * 储存桶名称
	 */
	public static final String COS_BUCKET_NAME ;
		
	/**
	 * 储存桶区域
	 */
	public static final String COS_REGION ;
		
	/**
	 * appid
	 */
	public static final String COS_APPID;
	
	/**
	 * 调用者身份
	 */
	public static final String COS_SECRETKEY;
	
	/**
	 * 秘钥
	 */
	public static final String COS_SECRETID;
	
	/**
	 * 允许上传路径
	 */
	public static final String COS_ALLOW_PREFIX;
	
	/**
	 * 临时秘钥时长(单位/秒)
	 */
	public static final String COS_DURATIONSECONDS;
	
	/**
	 * 项目名称
	 */
	public static final String COS_PROJECT_NAME;

	
	static {
		// 初始化cos信息
		ResourceBundle bundle = ResourceBundle.getBundle("properties/tencent");
		// 储存桶的名称
		COS_BUCKET_NAME = bundle.getString("tencent.bucketName");
		// 区域
		COS_REGION = bundle.getString("tencent.region");
		// appId
		COS_APPID = bundle.getString("tencent.appId");		
		// SecretId 是用于标识 API 调用者的身份
		COS_SECRETID = bundle.getString("tencent.SecretId");
		// SecretKey是用于加密签名字符串和服务器端验证签名字符串的密钥
		COS_SECRETKEY = bundle.getString("tencent.SecretKey");		
		//允许上传路径
		COS_ALLOW_PREFIX = bundle.getString("tencent.allowPrefix");	
		//临时秘钥时长(单位/秒)
		COS_DURATIONSECONDS = bundle.getString("tencent.durationSeconds");	
		//上传项目名称
		COS_PROJECT_NAME = bundle.getString("tencent.projectName");
	}		
}
