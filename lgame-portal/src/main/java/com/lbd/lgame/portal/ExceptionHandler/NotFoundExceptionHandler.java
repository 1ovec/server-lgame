package com.lbd.lgame.portal.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.lbd.lgame.common.exception.CommonEnum;
import com.lbd.lgame.common.utils.JsonResult;

@Controller
public class NotFoundExceptionHandler implements ErrorController {
	  private static final Logger logger = LoggerFactory.getLogger(NotFoundExceptionHandler.class);
	
    @Override
    public String getErrorPath() {
        return "/error";
    }
 
    @RequestMapping(value = {"/error"})
    @ResponseBody
    public JsonResult<String> error(HttpServletRequest request) {
    	 logger.error("发生业务异常！原因是：未找到该资源!");
    	 return JsonResult.error(CommonEnum.NOT_FOUND);
    }
}