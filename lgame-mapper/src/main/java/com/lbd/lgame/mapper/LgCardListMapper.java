package com.lbd.lgame.mapper;

import java.util.List;

import com.lbd.lgame.model.LgCardList;

/**
 * @author zhouhua
 * 2020-01-14
 * I am not responsible for this code。
 */
public interface LgCardListMapper {
	
	List<LgCardList> queryCardList(String cardType);

}
