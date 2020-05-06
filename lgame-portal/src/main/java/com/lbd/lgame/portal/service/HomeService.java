package com.lbd.lgame.portal.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.lbd.lgame.mapper.PubNoticeMapper;
import com.lbd.lgame.model.PubNotice;

@Service
public class HomeService {
	
	private static final Logger log = LoggerFactory.getLogger(HomeService.class);
	
	@Autowired
	private PubNoticeMapper pubNoticeMapper;
	
	
	public List<PubNotice> getPubNoticeList(Integer pageSize, Integer pageNum){
			log.info("查询通知列表");
			PageHelper.startPage(pageNum,pageSize);
			List<PubNotice> pubNoticeList = pubNoticeMapper.getPubNoticeList();
			log.info("pubNoticeList: "+pubNoticeList.size());
			return pubNoticeList;		
	}
	
	

}
