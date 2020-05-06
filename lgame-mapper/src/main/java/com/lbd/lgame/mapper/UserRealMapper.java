package com.lbd.lgame.mapper;

import org.apache.ibatis.annotations.Param;

import com.lbd.lgame.model.UserReal;

public interface UserRealMapper {

	/**
	 * 保存App用户认证信息
	 * @param userReal
	 * @return
	 */
	public int saveUserReal(UserReal userReal);
	
	/**
	 * 根据用户ID修改app认证信息表
	 * @param userReal
	 * @return
	 */
	public int updateUserRealByUserId(UserReal userReal);
	
	/**
	 * 查询用户是否有记录
	 * @param userId
	 * @return
	 */
	public int queryUserCount(@Param("userId") String userId);
		
}
