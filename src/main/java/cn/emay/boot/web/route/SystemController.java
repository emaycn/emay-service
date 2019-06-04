package cn.emay.boot.web.route;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.emay.boot.web.auth.WebAuth;

@Controller
@RequestMapping(method = RequestMethod.GET)
public class SystemController {

	/**
	 * 用户管理页面
	 */
	@WebAuth({ "USER_SELECT" })
	@RequestMapping("/user")
	public String user() {
		return "system/user";
	}
	
	/**
	 * 用户管理页面
	 */
	@WebAuth({ "ROLE_SELECT" })
	@RequestMapping("/role")
	public String role() {
		return "system/role";
	}

}
