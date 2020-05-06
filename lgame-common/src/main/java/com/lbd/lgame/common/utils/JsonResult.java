package com.lbd.lgame.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.lbd.lgame.common.api.CommonPage;
import com.lbd.lgame.common.exception.BaseErrorInfoInterface;

/**
 * JSON数据返回集合
 * @author junqing.cao
 * @date 2013-4-10
 * @param <T>
 */
public class JsonResult<T> {
    private static final Logger paramsLog = LoggerFactory.getLogger("paramsLog");
    
    
    /**
     * 标识成功失败<br>
     *  true 成功<br>
     *  false 失败
     */
    private boolean success;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 返回数据集合
     */
    private T data;

    /**
     * 分页标签
     */
    private CommonPage pager;
    
   



	/**
     * 默认构造函数
     */
    public JsonResult() {
    }
    
    /**
     * 2019-01-02新增
     */
    public static JsonResult<Object> security(){
    	return new JsonResult<Object>(false,"暂未登录或token已经过期");
    }
    
    /**
     * 2019-01-03新增
     */
    public static JsonResult<Object> forbidden(){
    	return new JsonResult<Object>(false,"没有相关权限");
    }

    /**
     * 构造函数
     * @param success 标识成功失败
     */
    public JsonResult(boolean success) {
        this.success = success;
        this.print();
    }

    /**
     * 构造函数
     * @param success 标识成功失败
     * @param msg 提示信息
     */
    public JsonResult(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
        this.print();
    }    
    

    /**
     * 构造函数
     * @param suggestAction 提示类型
     * @param msg 提示信息
     */
    public JsonResult(String msg) {
        this.success = true;
        this.msg = msg;
        this.print();
    }

    /**
     * 赋值主体数据
     * @param obj 主体数据对象
     * @return <JsonResult>
     */
    public JsonResult<T> convtData(T obj) {
        this.data = obj;
        this.print();
        return this;
    }

    /**
     * 赋值主题数据
     * @param obj 主体数据对象
     * @param pager 分页标签对象
     * @return <JsonResult>
     */
    public JsonResult<T> convtData(T obj, CommonPage pager) {
        this.data = obj;
        this.pager = pager;
        this.print();
        return this;
    }
    
    
    public boolean isSuccess() {
        return success;
    }


    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public CommonPage getPager() {
        return pager;
    }
    
	/**
     * 失败
     */
    public static JsonResult<String> error(BaseErrorInfoInterface errorInfo) {   	
        return new JsonResult<String>(errorInfo.getSuccess(),errorInfo.getMsg());
    }
    
    /**
     * 打印数据
     *
     * @author Joakim
     * @date 2014-10-15
     * @version 1.0.0
     */
    private void print() {
        paramsLog.info(JSON.toJSONString(this));
    }
}
