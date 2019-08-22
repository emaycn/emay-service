package cn.emay.boot.business.system.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.emay.boot.base.web.WebAuth;
import cn.emay.boot.business.system.pojo.User;
import cn.emay.boot.business.system.service.UserService;
import cn.emay.boot.utils.WebUtils;
import cn.emay.utils.encryption.Md5;
import cn.emay.utils.result.Result;

/**
 * 系统其他API
 * 
 * @author frank
 *
 */
@RestController
@RequestMapping(method = RequestMethod.POST)
public class SystemApi {

	@Autowired
	private UserService userService;

	/**
	 * 修改密码
	 */
	@WebAuth
	@RequestMapping(value = "/changePassword")
	public Result changepass(String password, String newpass) throws IOException {
		if (password == null) {
			return Result.badResult("原密码不能为空");
		}
		if (newpass == null) {
			return Result.badResult("新密码不能为空");
		}
		User user = WebUtils.getCurrentUser();
		String pass = Md5.md5(password.getBytes());
		if (!user.getPassword().equals(pass)) {
			return Result.badResult("密码错误");
		}
		userService.modifyPassword(user.getId(), newpass);
		return Result.rightResult();
	}

}
