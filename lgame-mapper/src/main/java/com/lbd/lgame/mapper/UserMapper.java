package com.lbd.lgame.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.lbd.lgame.model.User;

public interface UserMapper extends Mapper {
	
	int testUserCount();
	
	List<User> checkUser(User user);
	
	/**
	 * 注册信息保存
	 * @param user
	 * @return
	 */
	int saveAppUser(User user);
	
	/**
	 * 查询手机号是否存在
	 * @param userTel
	 * @return
	 */
	int queryTelCount(String userTel);
	
	/**
	 * 用户登录
	 * @param userTel
	 * @param userPasswd
	 * @return
	 */
	User userLogin(@Param("userTel") String userTel, @Param("userPasswd") String userPasswd);
	
	/**
	 * 用户信息修改
	 * @param user
	 * @return
	 */
	int updateUserInfo(User user);
	
	
	/**
	 * 根据用户ID修改用户实名认证状态
	 * @param user
	 * @return
	 */	
	public int updateRealStatusByUserId(@Param("realStatus")String realStatus,@Param("userId")String userId);
	
	
	/**
	 * 根据用户手机号验证用户是否合法
	 * @param userTel
	 * @return
	 */
	public User queryUserByUserTel(@Param("userTel") String userTel);
	
}
