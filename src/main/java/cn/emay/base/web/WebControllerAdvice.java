package cn.emay.base.web;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.support.WebBindingInitializer;

import cn.emay.constant.web.WebConstant;

/**
 * web-controller增强类<br/>
 * controller接收参数时的类型转换
 * 
 * @author HS
 *
 */
@ControllerAdvice
public class WebControllerAdvice implements WebBindingInitializer {

	@Override
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat dateFormat = new SimpleDateFormat(WebConstant.PARAMETER_DATE_FORMAT);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.registerCustomEditor(Short.class, new CustomNumberEditor(Short.class, true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
		binder.registerCustomEditor(Float.class, new CustomNumberEditor(Float.class, true));
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, true));
		binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, true));
		binder.registerCustomEditor(BigInteger.class, new CustomNumberEditor(BigInteger.class, true));
	}
}
