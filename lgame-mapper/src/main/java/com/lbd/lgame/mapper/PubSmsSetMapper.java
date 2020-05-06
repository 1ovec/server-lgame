package com.lbd.lgame.mapper;

import org.apache.ibatis.annotations.Param;
import com.lbd.lgame.model.PubSmsSet;


public interface PubSmsSetMapper {
	
	/**
	 * 根据短信类型检索短信模板配置
	 * @param smsType
	 * @return
	 */
	public PubSmsSet querySmsSetBySmsType(@Param("smsType")String smsType);	
}
