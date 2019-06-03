package cn.emay.boot.web.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.emay.boot.common.WebConstant;
import cn.emay.boot.utils.CaptchaUtils;
import cn.emay.boot.web.utils.WebUtils;
import cn.emay.redis.RedisClient;

/**
 * @author 东旭
 */
@Controller
public class PublicController {

	@Autowired
	private RedisClient redis;

	/**
	 * 初始页面
	 */
	@RequestMapping("/")
	public String indexDefault() {
		if (!WebUtils.isLogin()) {
			return "redirect:/login";
		} else {
			return "index";
		}
	}

	/**
	 * 初始页面
	 */
	@RequestMapping("/index")
	public String index() {
		return indexDefault();
	}

	/**
	 * 错误页面
	 */
	@RequestMapping("/error")
	public String error() {
		return "error";
	}

	/**
	 * 验证码图片
	 */
	@RequestMapping("/captcha")
	public void captcha() throws Exception {
		HttpServletResponse response = WebUtils.getCurrentHttpResponse();
		String tokenId = WebUtils.getSessionId();
		CaptchaUtils.writeByRedis(redis, response, tokenId, WebConstant.CAPTCHA_TAG_LOGIN, 1800);
	}

	/**
	 * 登录页面
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String toLogin() {
		if (WebUtils.isLogin()) {
			return "redirect:/";
		} else {
			return "login";
		}
	}

}
