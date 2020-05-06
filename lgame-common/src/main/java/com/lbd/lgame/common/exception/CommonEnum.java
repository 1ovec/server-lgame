package com.lbd.lgame.common.exception;

public enum CommonEnum implements BaseErrorInfoInterface {
    // 数据操作错误定义
    SUCCESS(true, "成功!"), 
    BODY_NOT_MATCH(false,"请求的数据格式不符!"),
    SIGNATURE_NOT_MATCH(false,"请求的数字签名不匹配!"),
    NOT_FOUND(false, "未找到该资源!"), 
    INTERNAL_SERVER_ERROR(false, "服务器内部错误!"),
    SERVER_BUSY(false,"服务器正忙，请稍后再试!")
    ;

    /** 标识成功失败<br> */
    private Boolean success;

    /** 错误描述 */
    private String msg;

    CommonEnum(Boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

	@Override
	public boolean getSuccess() {
		// TODO Auto-generated method stub
		return success;
	}

	@Override
	public String getMsg() {
		// TODO Auto-generated method stub
		return msg;
	}
}
