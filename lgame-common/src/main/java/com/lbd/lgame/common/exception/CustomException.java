package com.lbd.lgame.common.exception;

public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    /**
     * 错误信息
     */
    protected String msg;

    public CustomException() {
        super();
    }

    public CustomException(BaseErrorInfoInterface errorInfoInterface) {
        super(errorInfoInterface.getMsg());
        this.msg = errorInfoInterface.getMsg();
    }
    
    public CustomException(BaseErrorInfoInterface errorInfoInterface, Throwable cause) {
        super(errorInfoInterface.getMsg(), cause);
        this.msg = errorInfoInterface.getMsg();
    }
    
    public CustomException(String errorMsg) {
        super(errorMsg);
        this.msg = errorMsg;
    }
    
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
