package com.lbd.lgame.mapper;

import com.lbd.lgame.model.PubImage;

public interface PubImageMapper {
	
	
	/**
	 * 保存上传图片信息
	 * @param pubImage
	 * @return
	 */
	public int savePubImage(PubImage pubImage);
}
