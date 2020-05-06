package com.lbd.lgame.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lbd.lgame.model.PubButton;

public interface PubButtonMapper {
	
	/**
	 * 查询所有
	 * @return
	 */
	List<PubButton> getListInfo();
	
	/**
	 * 根据类型查询开关
	 * @param bType
	 * @return
	 */
	PubButton getPubButtonInfo(@Param("bType") String bType);


	/**
	 * 根据字典编号和名称检索开关表
	 * @param dicCode
	 * @param dicName
	 * @return
	 */
	public PubButton queryButtonByDicCode(@Param("dicCode")String dicCode,@Param("dicName") String dicName);
		

}
