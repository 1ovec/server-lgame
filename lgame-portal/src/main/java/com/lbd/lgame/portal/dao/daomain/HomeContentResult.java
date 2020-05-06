package com.lbd.lgame.portal.dao.daomain;

import java.util.List;

import com.lbd.lgame.model.PubNotice;

public class HomeContentResult {
	
	/**
	 * 通知类
	 */
	private List<PubNotice>  pubNoticeList;

	public List<PubNotice> getPubNoticeList() {
		return pubNoticeList;
	}

	public void setPubNoticeList(List<PubNotice> pubNoticeList) {
		this.pubNoticeList = pubNoticeList;
	}
	
	
	

}
