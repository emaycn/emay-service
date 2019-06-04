package cn.emay.boot.web.route;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.emay.utils.result.Result;

@Controller
@RequestMapping("/error")
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

	@Override
	public String getErrorPath() {
		return "error";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String error(HttpServletRequest request, HttpServletResponse response) {
		return getErrorPath();
	}

	@RequestMapping(method = RequestMethod.POST)
	public Result errorAjax(HttpServletRequest request, HttpServletResponse response) {
		String msg = request.getParameter("msg");
		return Result.badResult("-1", msg == null ? "request not found" : msg, null);
	}

	protected HttpStatus getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		try {
			return HttpStatus.valueOf(statusCode);
		} catch (Exception ex) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
	}

}
