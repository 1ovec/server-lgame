package com.ldb.lgame.sms.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lbd.lgame.mapper.PubBaseDicMapper;
import com.lbd.lgame.model.PubBaseDic;



@Service
public class BaseDicService {
   
    @Autowired
	private PubBaseDicMapper baseDicDao;
   
	/**
	 * 根据类型查询字典
	 */
	public List<PubBaseDic> queryBaseByDicType(String dicType){
		return baseDicDao.queryBaseByDicType(dicType);
	}	
}
