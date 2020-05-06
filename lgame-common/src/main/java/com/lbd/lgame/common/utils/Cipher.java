package com.lbd.lgame.common.utils;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbd.lgame.commons.encryption.Base64Coder;
import com.lbd.lgame.commons.encryption.MD5Coder;
import com.lbd.lgame.commons.encryption.RSACoderByIOS;
import com.lbd.lgame.commons.encryption.RSACoderNotIOS;


/** 
 * 密码处理类
 *
 * @author junqing.cao
 * @date 2013-5-28
 */
public class Cipher {
    private static final Logger log = LoggerFactory.getLogger(Cipher.class);
    
    
    /** 
     * 管理员密码混杂字符串
     */
    public static final String PWD_ADMIN_MIX = "@yue.admin";
    
    /** 
     * 用户密码混杂字符串
     */
    public static final String PWD_USER_MIX = "@yue.user";
    
    /** 
     * 技师密码混杂字符串
     */
    public static final String PWD_TECH_MIX = "@yue.tech";
    /**
     * 密码最短长度
     */
    public static final Integer CIPHER_MIN_LEN =6;
    /**
     * 密码最长长度
     */
    public static final Integer CIPHER_MAX_LEN=16;
    
    /**
     * WEB端用户密码混杂字符串
     * */
    public static final String PWD_USER_WEB = "@car.user.web";
    
    /**
     * WEB端用户支付密码加密
     */
    public static final String APWD_USER_WEB = "@car.user.apwd";
    
    /**
     * 用户身份证加密
     */
    public static final String USER_SID = "@car.user.sid";
    
    
    
    
    private Cipher() {}
    
    
    private static final String PWD_ERR_LEN = "密码长度必须为" + CIPHER_MIN_LEN + "至" + CIPHER_MAX_LEN + "之间";

    
    /**
     * 解码
     * @param pwd
     * @param clientType
     * @return
     * @throws Exception
     *
     * @author Joakim
     * @date 2014-10-20
     * @version 1.0.0
     */
    public static String decryptCipher(String pwd, Client clientType) throws Exception {
        if (clientType == Client.UA) {
            return decryptCipher_android(pwd, clientType);
        }
        else if (clientType == Client.UI) {
            return decryptCipher_ios(pwd, clientType);
        }
        
        return pwd;
    }
    
    
    /**
     * 解密IOS客户端上送密码
     * @param pwd 加密字符串
     * @param clientType<Client> 客户端类型
     * @return
     * @throws Exception
     *
     * @author Joakim
     * @date 2014-10-27
     * @version 1.0.0
     */
    private static String decryptCipher_ios(String pwd, Client clientType) throws Exception {
        log.debug("IOS解码开始");
        String md5Pwd = "";
        byte[] bs = null;

        bs = Base64Coder.decodeStr(pwd.replaceAll(" ", "+"));
        String str = new String(RSACoderByIOS.getInstance().decrypt(bs));
        
        // 验证密码长度
        if (!checkCipherLen(str)) {
            throw new Exception(PWD_ERR_LEN);
        }
        
        if (Client.UI.equals(clientType)) { // IOS用户端
            md5Pwd = encryptChiper(str + PWD_USER_MIX);
        }
        else { // IOS技师端
            md5Pwd = encryptChiper(str + PWD_TECH_MIX);
        }
        log.debug("IOS解码结束:" + md5Pwd);
        return md5Pwd;
    }
    
    /**
     * 解密Android客户端上送密码
     * @param pwd 加密字符串
     * @param clientType<Client> 客户端类型
     * @return
     * @throws Exception
     *
     * @author Joakim
     * @date 2014-10-27
     * @version 1.0.0
     */
    private static String decryptCipher_android(String pwd, Client clientType) throws Exception {
        log.debug("Android解码开始");
        String md5Pwd = "";
        byte[] bs = null;
        bs = Base64Coder.decodeStr(pwd.replaceAll(" ", "+"));
        String str = new String(RSACoderNotIOS.getInstance().decrypt(bs));
        
        // 验证密码长度
        if (!checkCipherLen(str)) {
            throw new Exception(PWD_ERR_LEN);
        }
        
        if (Client.UA.equals(clientType)) { // Android用户端
            md5Pwd = encryptChiper(str + PWD_USER_MIX);
        }
        else { // Android技师端
            md5Pwd = encryptChiper(str + PWD_TECH_MIX);
        }
        log.debug("Android解码结束:" + md5Pwd);
        return md5Pwd;
    }

    /**
     * 管理员密码加密
     *
     * @param pwd
     * @return
     *
     * @auther junqing.cao
     * @date 2013-6-8
     * @version 1.0.0
     */
    public static String encryptAdminChiper(String pwd) {
        log.debug("管理员密码加密开始");
        String md5Pwd = "";
        
        md5Pwd = encryptChiper(pwd + PWD_ADMIN_MIX);
        log.debug("管理员密码加密结束:" + md5Pwd);
        return md5Pwd;
    }

    /**
     * 密码加密
     * @param pwd
     * @return
     *
     * @author Joakim
     * @date 2014-10-20
     * @version 1.0.0
     */
    private static String encryptChiper(String pwd) {
        return MD5Coder.encrypt(pwd);
    }
    
    /**
     * WEB端用户密码加密
     * @param pwd
     * @return
     */
    public static String encryptWebUserChiper(String pwd) {
        log.debug("WEB端用户密码加密开始");
        String md5Pwd = "";
        
        md5Pwd = encryptChiper(pwd + PWD_USER_WEB);
        
        log.debug("WEB端用户密码加密结束:" + md5Pwd);
        return md5Pwd;
    }
    
    /**
     * WEB端支付密码加密
     */
    public static String encryptAPwdUserChiper(String pwd){
    	log.debug("WEB端用户支付密码加密开始");
    	String md5Pwd = "";
    	md5Pwd = encryptChiper(pwd + APWD_USER_WEB);
    	 log.debug("WEB端用户支付密码加密:" + md5Pwd);
    	return md5Pwd;
    }
    
    /**
     * WEB用户身份证加密
     */
    public static String encryptWebUserSid(String sid){
    	log.debug("WEB端用户身份证加密开始");
    	String md5Sid = "";
    	md5Sid = encryptChiper(sid + USER_SID);
    	log.debug("WEB端用户身份证加密结束："+md5Sid);
    	return md5Sid;
    }
    
    /**
     * 验证密码长度是否符合规则
     * @param cipher 密码
     * @return
     *
     * @author Joakim
     * @date 2013-11-29
     * @version 1.0.0
     */
    public static boolean checkCipherLen(String cipher) {
        boolean flag = false;
        if (cipher != null && cipher.length() >= CIPHER_MIN_LEN && cipher.length() <= CIPHER_MAX_LEN) {
            flag = true;
        }
        return flag;
    }
}
