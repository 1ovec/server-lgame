package com.lbd.lgame.portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lbd.lgame.mapper.PubImageMapper;
import com.lbd.lgame.model.PubImage;

@Service
public class PubImageService {
	@Autowired
	private PubImageMapper pubImageDao;
	
	/**
	 * 保存上传图片信息
	 * @param pubImage
	 * @return
	 */
	public int savePubImage(PubImage pubImage) {
		return pubImageDao.savePubImage(pubImage);
	}
}
