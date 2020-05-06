package com.lbd.lgame.mapper;

import com.lbd.lgame.model.view.UserPayCardView;

/**
 * @author zhouhua
 * 2020-01-15
 * I am not responsible for this code。
 */
public interface UserPayCardMapper {
	
	int queryUserPayCardCount(String userID);
	
	UserPayCardView getUserPayCardInfo(String userID);

}
