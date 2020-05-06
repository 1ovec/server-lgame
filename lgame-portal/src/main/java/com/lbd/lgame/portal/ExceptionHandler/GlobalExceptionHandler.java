package com.lbd.lgame.portal.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import com.lbd.lgame.common.exception.CommonEnum;
import com.lbd.lgame.common.exception.CustomException;
import com.lbd.lgame.common.utils.JsonResult;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * 处理自定义的业务异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = CustomException.class)  
    @ResponseBody  
    public  JsonResult<String> bizExceptionHandler(HttpServletRequest req, CustomException e){
        logger.error("发生业务异常！原因是：{}",e.getMsg());
        return new JsonResult<>(false, e.getMsg());
    }

    /**
     * 处理空指针的异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =NullPointerException.class)
    @ResponseBody
    public JsonResult<String> exceptionHandler(HttpServletRequest req, NullPointerException e){
        logger.error("发生空指针异常！原因是:{}",e);
        return JsonResult.error(CommonEnum.BODY_NOT_MATCH);
    }


    /**
      * 处理其他异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public JsonResult<String> exceptionHandler(HttpServletRequest req, Exception e){
        logger.error("未知异常！原因是:{}",e);
        return JsonResult.error(CommonEnum.INTERNAL_SERVER_ERROR);
    }
}
