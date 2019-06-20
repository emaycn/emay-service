package cn.emay.boot.base.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.emay.utils.result.Result;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result jsonErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        return Result.badResult("request fail");
    }

}