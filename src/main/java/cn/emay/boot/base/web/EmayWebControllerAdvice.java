package cn.emay.boot.base.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.support.WebBindingInitializer;

import cn.emay.boot.base.constant.WebConstant;
/**
 * emay-web-controller增强类
 * @author HS
 *
 */
@ControllerAdvice
public class EmayWebControllerAdvice implements WebBindingInitializer {

	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		 //转换日期
        DateFormat dateFormat=new SimpleDateFormat(WebConstant.PARAMETER_DATE_FORMAT);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
