/**  
 * @Title: JsonUtil.java
 * @Package com.newland.java.common.util
 * @Description JSON转换工具类
 * @author Joakim
 * @date 2015年8月31日 上午9:28:17
 * @version V1.0  
 */
package com.lbd.lgame.common.tools;

import java.lang.reflect.Type;

import com.google.gson.Gson;

public class JsonUtil {
	private static Gson gson = new Gson();
	
	
	/**
	 * 
	 * @Title toJson
	 * @Description 转换实体对象为JSON字符串
	 * @param obj 泛型对象
	 * @return
	 */
	public static <T> String toJson(T obj) {
//		Gson gson = new Gson();
		return gson.toJson(obj);
	}
	
	/**
	 * 
	 * @Title fromJson
	 * @Description json字符串转对象
	 * @param jsonStr
	 * @param clz
	 * @return
	 */
	public static <T> T fromJson(String jsonStr, Class<T> clz) {
//		Gson gson = new Gson();
		return gson.fromJson(jsonStr, clz);
	}
	
	/**
	 * 
	 * @Title fromJson
	 * @Description 转换复杂类型对象
	 * @param jsonStr
	 * @param type
	 * @return
	 */
	public static <T> T fromJson(String jsonStr, Type type) {
//		Gson gson = new Gson();
		return gson.fromJson(jsonStr, type);
	}
}
