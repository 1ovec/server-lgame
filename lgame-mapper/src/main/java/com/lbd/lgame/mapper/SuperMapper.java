package com.lbd.lgame.mapper;


import org.apache.ibatis.annotations.Mapper;

/**
 *  mapper 父类，注意这个类不要让 mp 扫描到！！
 */
public interface SuperMapper<T> extends Mapper {

	// 这里可以放一些公共的方法
}
