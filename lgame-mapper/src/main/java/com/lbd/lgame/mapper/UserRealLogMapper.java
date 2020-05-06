package com.lbd.lgame.mapper;

import com.lbd.lgame.model.UserRealLog;


public interface UserRealLogMapper {

	/**
	 * 保存信息到app用户认证历史记录表
	 * @param userRealLog
	 * @return
	 */
	public int saveUserRealLog(UserRealLog userRealLog);
	
	/**
	 * 根据ID修改实名认证历史记录表信息
	 * @param userRealLog
	 * @return
	 */
	public int updateUserRealLogById(UserRealLog userRealLog);
			
}
