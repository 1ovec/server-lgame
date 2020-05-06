package com.lbd.lgame.portal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestParam;

import com.lbd.lgame.common.utils.Cipher;
import com.lbd.lgame.common.utils.Client;
import com.lbd.lgame.common.utils.JsonResult;
import com.lbd.lgame.common.utils.StringUtils;
import com.lbd.lgame.common.utils.Utils;
import com.lbd.lgame.mapper.UserMapper;
import com.lbd.lgame.model.User;
import com.lbd.lgame.portal.dao.daomain.AppUserDetails;
import com.ldb.lgame.security.util.JwtTokenUtil;



@Service
public class UserService {
	
	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
    private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private FeignRedisService feignRedisService;
	
	@Autowired
	private FeignSmsService feignSmsService;
	
	
	/**
	 * 判断是否查询到数据
	 * @param userTel
	 * @return
	 */
	public User getUserInfo(String userTel,String status) {
		User userExample = new User();
		userExample.setUserTel(userTel);
		userExample.setStatus(status);
		List<User> userList = userMapper.checkUser(userExample);
		if(!CollectionUtils.isEmpty(userList)) {
			return userList.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * 查询用户信息
	 * @param userTel
	 * @param status
	 * @return
	 */
	public UserDetails loadUserByUsername(String userTel,String status) {
		User user = getUserInfo(userTel,status);
		if( user !=null) {
			return new AppUserDetails(user);
		} else {
			throw new UsernameNotFoundException("用户名或密码错误");
		}
	}
	
	/**
	 * 用户注册
	 * @param phoneNo
	 * @param pwd
	 * @param verifyCode
	 * @param clientType
	 * @param systemVersion
	 * @param deviceVersion
	 * @param deviceBrand
	 * @return
	 */
	public JsonResult<String> reg(String userTel, String pwd, 
				String verifyCode,Client clientType,String systemVersion,String deviceVersion,String deviceBrand) throws Exception {
		
		JsonResult<String> jr = null;
		
		String success = checkRegParams(userTel, pwd, verifyCode, clientType, systemVersion, deviceVersion, deviceBrand);
		if(!"SUCCESS".equals(success)) {
			return new JsonResult<String>(false,success);
		}
		
		//判断手机号是否存在
		int countTel = userMapper.queryTelCount(userTel);
		if(countTel >0) {
			return  new  JsonResult<String>(false,"该手机号已注册！").convtData(null);
		} 
		
		//验证码校验
		JsonResult<String> jrsms = feignRedisService.checkSmsVerifyCode(userTel, "01", verifyCode);
		if(!jrsms.isSuccess()) {
			 new  JsonResult<User>(false,jrsms.getMsg());
		}
		
		User user = new User();
		user.setUserTel(userTel);
		user.setUserPasswd(Cipher.decryptCipher(pwd, clientType));
		user.setDeviceBrand(deviceBrand);
		user.setSystemVersion(systemVersion);
		user.setDeviceVersion(deviceVersion);
		int count = userMapper.saveAppUser(user);
		if(count >0) {
			jr = new  JsonResult<String>(true,"注册成功");
		} else {
			jr = new  JsonResult<String>(false,"注册失败");
		}
		return jr;	
	}
	
	/**
	 * 用户登录
	 * @param userTel
	 * @param pwd
	 * @param cLient
	 * @return
	 */
	public JsonResult<Object> login(String userTel, String pwd,Client cLient,String verifyCode,String loginType){
		JsonResult<Object> jr = null;
		String token = null;
		User checkUser = getUserInfo(userTel,null);
		if(checkUser == null) {
			return new JsonResult<Object>(false,"此用户不存在");
		}
		if("2".equals(checkUser.getStatus())) {
			return new JsonResult<Object>(false,"此用户已被冻结");
		}
		
		User user = new User();
		if("01".equals(loginType)) {
			user = userMapper.userLogin(userTel, pwd);
			if(user == null) {
				return new JsonResult<Object>(false,"账户或密码错误!");
			}
		}
		
		if("02".equals(loginType)) {
			JsonResult<String> smsJr =checkSmsVerifyCode(userTel,"01",verifyCode);
			if(!smsJr.isSuccess()) {
				return  new JsonResult<Object>(false,smsJr.getMsg());
			} else {
				user = userMapper.userLogin(userTel, null);;
			}
		}
		
		//封装
		UserDetails userDetails = new AppUserDetails(user);
		
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        token = jwtTokenUtil.generateToken(userDetails);
        Map<String, String> map = new HashMap<String, String>();
        map.put("userTel", user.getUserTel());
        map.put("userId", user.getUserId());
        map.put("token", token);
        map.put("tokenHead", tokenHead);
        jr = new JsonResult<Object>(true,"登录成功！").convtData(map);	
		return jr;
	}
	
	/**
	 * token刷新
	 * @param request
	 * @return
	 */
	public JsonResult<Object> refreshToken(HttpServletRequest request) {
		 String token = request.getHeader(tokenHeader);
		 String refreshToken = jwtTokenUtil.refreshHeadToken(token);
		 if(refreshToken == null) {
			 return new JsonResult<Object>(false, "token已经过期！");
		 }
		 Map<String,String> map = new HashMap<String,String>();
		 map.put("token", token);
		 map.put("tokenHead", tokenHead);
		 return new JsonResult<Object>(true, "刷新成功").convtData(map);
	}
	
	
	/**
	 * 找回密码
	 * @param userTel
	 * @param pwd
	 * @param verifyCode
	 * @param clientType
	 * @return
	 * @throws Exception
	 */
	public JsonResult<String> findPwd(String userTel,String pwd,String verifyCode,Client clientType) throws Exception{
		log.info("userTel密码修改:{},类型:{}",userTel,clientType);
		//查询手机号是否存在
		User user = getUserInfo(userTel,null);
		if(user == null) {
			return new JsonResult<String>(false, "手机号不存在！");
		}
		//查询手机号状态是否正常
		if("2".equals(user.getStatus())) {
			return new JsonResult<>(false,"此用户已被冻结");
		}
		JsonResult<String> smsJr =checkSmsVerifyCode(userTel,"03",verifyCode);
		if(!smsJr.isSuccess()) {
			return  new JsonResult<String>(false,smsJr.getMsg());
		} 
		User updateUser = new User();
		updateUser.setUserTel(userTel);
		updateUser.setUserPasswd(Cipher.decryptCipher(pwd, clientType));
		int count = userMapper.updateUserInfo(updateUser);
		if(count > 0) {
			return new JsonResult<>(true,"密码找回成功");
		} else {
			return new JsonResult<>(false,"密码修改失败");
		}	
	}
	
	/**
	 * 密码修改
	 * @param userTel
	 * @param oldPwd
	 * @param newPwd
	 * @param clientType
	 * @return
	 */
	public JsonResult<String> updatePwd(String userTel,String oldPwd,String newPwd,Client clientType) throws Exception{
		log.info("密码修改,userTel:{},clientType:{}",userTel,clientType);
		oldPwd = Cipher.decryptCipher(oldPwd, clientType);
		//判断原密码是否正确
		User user = userMapper.userLogin(userTel, oldPwd);
		if(user == null) {
			return new JsonResult<String>(false,"原密码错误!");
		}
		newPwd = Cipher.decryptCipher(newPwd, clientType);
		//密码修改
		User upUser = new User();
		upUser.setUserTel(userTel);
		upUser.setUserPasswd(newPwd);
		int count = userMapper.updateUserInfo(upUser);
		if(count > 0) {
			return new JsonResult<>(true,"密码修改成功");
		} else {
			return new JsonResult<>(false,"密码修改失败");
		}	
		
	}
	
	
	public String checkRegParams(String userTel, String pwd, 
			String verifyCode,Client clientType,String systemVersion,String deviceVersion,String deviceBrand) {
		//校验手机号码是否为空
		if (StringUtils.isEmpty(userTel)) {
			return "userTel参数必传";
		}
		//校验密码是否为空
		if (StringUtils.isEmpty(pwd)) {
			return "pwd参数必传";
		}
		//校验短信验证码是否为空
		if (StringUtils.isEmpty(verifyCode)) {
			return "verifyCode参数必传";
		}
		//校验用户端是否为空
		if (Utils.isEmpty(clientType)) {
			return "clientType参数必传";
		}
		//校验系统版本号是否为空
		if (StringUtils.isEmpty(systemVersion)) {
			return "clientType参数必传";
		}
		//校验手机型号是否为空
		if (StringUtils.isEmpty(deviceVersion)) {
			return "deviceVersion参数必传";
		}
		//校验手机厂商是否为空
		if (StringUtils.isEmpty(deviceBrand)) {
			return "deviceBrand参数必传";
		}		
		return "SUCCESS";
	}	
	
	/**
	 * 短信校验
	 * @param phone
	 * @param smsType
	 * @param verifyCode
	 * @return
	 */
	public JsonResult<String> checkSmsVerifyCode( String phone,String smsType, String verifyCode){
		return feignRedisService.checkSmsVerifyCode(phone, smsType, verifyCode);
	}
	
	/**
	 * 短信发送
	 * @param phoneNo
	 * @param smsType
	 * @return
	 */
	public JsonResult<String> sendSms(String phoneNo,String smsType){
		return  feignSmsService.sendSms(phoneNo, smsType);
	}

	/**
	 * 根据用户手机号验证用户是否合法
	 * @return
	 */
	public User queryUserByUserTel(String userTel) {
		return userMapper.queryUserByUserTel(userTel);		
	}
}
