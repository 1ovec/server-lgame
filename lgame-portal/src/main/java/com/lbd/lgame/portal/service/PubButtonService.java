package com.lbd.lgame.portal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lbd.lgame.mapper.PubButtonMapper;
import com.lbd.lgame.model.PubButton;

@Service
public class PubButtonService {
	
	private static final Logger log = LoggerFactory.getLogger(PubButtonService.class);
	
	@Autowired
	private PubButtonMapper pubButtonMapper;
	
	/**
	 * 查询开关信息
	 * @param bType
	 * @return
	 */
	public PubButton getPubButtonInfo(String bType) {
		return pubButtonMapper.getPubButtonInfo(bType);
	}

}
