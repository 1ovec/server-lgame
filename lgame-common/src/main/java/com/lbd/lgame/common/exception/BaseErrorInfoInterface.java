package com.lbd.lgame.common.exception;

public interface BaseErrorInfoInterface {
     /**
      * 标识成功失败<br>
      *  true 成功<br>
      *  false 失败
      */
      boolean getSuccess();

     /**
      * 提示信息
      */
     String getMsg();
}
