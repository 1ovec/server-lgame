package com.lbd.lgame.wish.service;
import com.lbd.lgame.common.utils.JsonResult;
import com.lbd.lgame.mapper.PubImageMapper;
import com.lbd.lgame.model.PubImage;
import com.lbd.lgame.model.UserWish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lbd.lgame.wish.dao.WishMapper;
import org.springframework.util.StringUtils;

/**
 * @author zhouhua
 * 2020-01-06
 * I am not responsible for this code。
 */
@Service
public class WishService {

	private static final Logger log = LoggerFactory.getLogger(WishService.class);

	@Autowired
	private WishMapper wishMapper;
	
	@Autowired
	private PubImageMapper pubImageDao;

	public String test() {
		return wishMapper.testInfo();
	}

	/*
	 * 用户心愿数据新增
	 * */
	public JsonResult<String> saveUserWish(String userId, String wishName, String wishText, String wishPath, String price) {
		//如果心愿图片路径不为空，添加到图片表
		if (!StringUtils.isEmpty(wishPath)){
			PubImage pubImage = new PubImage();
			//图片类型设置为01：心愿图片
			pubImage.setImageType("01");
			pubImage.setImagePath(wishPath);
			pubImageDao.savePubImage(pubImage);
		}
		//添加数据到用户心愿表
		UserWish userWish = new UserWish();
		userWish.setUserId(userId);
		userWish.setWishName(wishName);
		userWish.setWishText(wishText);
		userWish.setWishPath(wishPath);
		userWish.setPrice(price);
		int count = wishMapper.saveUserWish(userWish);
		if (count == 0) {
			return new JsonResult<String>(false, "心愿添加失败");
		} else {
			return new JsonResult<String>(true, "心愿添加成功");
		}
	}

}
