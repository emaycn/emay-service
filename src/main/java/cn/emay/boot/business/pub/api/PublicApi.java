package cn.emay.boot.business.pub.api;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.emay.boot.base.constant.WebConstant;
import cn.emay.boot.base.web.WebAuth;
import cn.emay.boot.base.web.WebToken;
import cn.emay.boot.business.system.pojo.Resource;
import cn.emay.boot.business.system.pojo.User;
import cn.emay.boot.business.system.service.ResourceService;
import cn.emay.boot.business.system.service.UserService;
import cn.emay.boot.utils.CaptchaUtils;
import cn.emay.boot.utils.PasswordUtils;
import cn.emay.boot.utils.WebUtils;
import cn.emay.redis.RedisClient;
import cn.emay.utils.result.Result;

/**
 * 公共API
 * 
 * @author Frank
 *
 */
@RestController
public class PublicApi {

	@Autowired
	private UserService userService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private RedisClient redis;

	/**
	 * 验证码图片
	 */
	@RequestMapping(value = "/loginCaptcha", method = RequestMethod.GET)
	public void loginCaptcha(String uuid) throws Exception {
		CaptchaUtils.writeByRedis(redis, uuid, WebConstant.CAPTCHA_TAG_LOGIN, 1800);
	}

	/**
	 * 登出
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public Result logout() {
		WebUtils.logout();
		return Result.rightResult();
	}

	/**
	 * 登录
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Result login(String username, String password, String captcha, String uuid) {
		if (StringUtils.isEmpty(username)) {
			return Result.badResult("用户名不能为空");
		}
		username = username.toLowerCase();
		if (StringUtils.isEmpty(password)) {
			return Result.badResult("密码不能为空");
		}
		if (StringUtils.isEmpty(captcha)) {
			return Result.badResult("验证码不能为空");
		}
		if (StringUtils.isEmpty(uuid)) {
			return Result.badResult("参数错误");
		}
		boolean isOk = CaptchaUtils.checkByRedis(redis, uuid, WebConstant.CAPTCHA_TAG_LOGIN, captcha);
		if (!isOk) {
			return Result.badResult("验证码错误");
		}
		User user = userService.findByUserName(username);
		if (user == null) {
			return Result.badResult("用户名或密码错误");
		}
		if (user.getUserState() == User.STATE_OFF) {
			return Result.badResult("用户已锁定，请联系管理员解锁");
		}
		String pass = PasswordUtils.encrypt(password);
		if (!user.getPassword().equalsIgnoreCase(pass)) {
			return Result.badResult("用户名或密码错误");
		}
		List<Resource> userResource = resourceService.getUserResources(user.getId());
		WebToken token = WebUtils.login(user, userResource);
		return Result.rightResult(token);
	}
	
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
		String pass = PasswordUtils.encrypt(password);
		if (!user.getPassword().equals(pass)) {
			return Result.badResult("密码错误");
		}
		String newpass1 = PasswordUtils.encrypt(newpass);
		userService.modifyPassword(user.getId(), newpass1);
		return Result.rightResult();
	}

}
