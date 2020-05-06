package com.lbd.lgame.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.lbd.lgame.model.PubBaseDic;


public interface PubBaseDicMapper {
	 /**
	  * 根据类型查询字典
	 * @param dicType
	 * @return
	 */
	public List<PubBaseDic> queryBaseByDicType(@Param("dicType")String dicType);
	
	/**
	 *  查询字典表每天允许发送短信最大次数 
	 * @return
	 */
	public int queryBaseDicSmsCount();
}
