package cn.emay.boot.base.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.emay.utils.result.Result;

@ControllerAdvice
public class GlobalExceptionHandler {

	private Logger log = LoggerFactory.getLogger(getClass());
	
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result jsonErrorHandler(HttpServletRequest req, Exception e) throws Exception {
    	log.error(e.getMessage(),e);
        return Result.badResult("request fail");
    }

}