package cn.emay.boot.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.emay.boot.web.auth.WebAuth;

@Controller
public class SystemController {

	/**
	 * 用户管理页面
	 */
	@WebAuth({ "USER_SELECT" })
	@RequestMapping("/user")
	public String user() {
		return "user";
	}
	
	/**
	 * 用户管理页面
	 */
	@WebAuth({ "ROLE_SELECT" })
	@RequestMapping("/role")
	public String role() {
		return "role";
	}

}
