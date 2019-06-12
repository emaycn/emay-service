package cn.emay.boot.restful.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.emay.utils.result.Result;

@Controller
public class HttpErrorController implements ErrorController {

    private final static String ERROR_PATH = "/error";

    @RequestMapping(value = ERROR_PATH)
    @ResponseBody
    public Result error(HttpServletRequest request) {
    	 return Result.badResult("request fail");
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
    
}