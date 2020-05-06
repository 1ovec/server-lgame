package com.lbd.lgame.mapper;

import com.lbd.lgame.model.PubSms;

public interface PubSmsMapper {

	
	/**
	 * 保存信息到短信表
	 * @param sms
	 * @return
	 */
	public int saveSms(PubSms sms);
	
	
	/**
	 * 根据ID更改短信表信息
	 * @param sms
	 * @return
	 */
	public int updateSmsById(PubSms sms); 	
}
