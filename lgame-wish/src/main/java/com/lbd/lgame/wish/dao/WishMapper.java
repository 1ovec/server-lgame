package com.lbd.lgame.wish.dao;
import com.lbd.lgame.model.UserWish;

/**
 * @author zhouhua
 * 2020-01-06
 * I am not responsible for this code。
 */
public interface WishMapper {
	
	String testInfo();

    /*
     * 用户心愿数据新增
     * */
    public int saveUserWish(UserWish userWish);
}
