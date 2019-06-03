package cn.emay.boot.restful.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.emay.boot.common.WebConstant;
import cn.emay.boot.pojo.system.Resource;
import cn.emay.boot.pojo.system.User;
import cn.emay.boot.service.system.ResourceService;
import cn.emay.boot.service.system.UserService;
import cn.emay.boot.utils.CaptchaUtils;
import cn.emay.boot.web.utils.WebUtils;
import cn.emay.redis.RedisClient;
import cn.emay.utils.encryption.Md5;
import cn.emay.utils.result.Result;

@RestController
public class PublicRestController {

	@Autowired
	private UserService userService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private RedisClient redis;

	/**
	 * 登出
	 */
	@RequestMapping(value = "/logout")
	public Result logout() {
		WebUtils.logout();
		return Result.rightResult();
	}

	/**
	 * 登录
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Result login(String username, String password, String captcha) {
		if (StringUtils.isEmpty(username)) {
			return Result.badResult("用户名不能为空");
		}
		if (StringUtils.isEmpty(password)) {
			return Result.badResult("密码不能为空");
		}
		if (StringUtils.isEmpty(captcha)) {
			return Result.badResult("验证码不能为空");
		}
		String tokenId = WebUtils.getSessionId();
		boolean isOk = CaptchaUtils.checkByRedis(redis, tokenId, WebConstant.CAPTCHA_TAG_LOGIN, captcha);
		if (!isOk) {
			return Result.badResult("验证码错误");
		}
		User user = userService.findByUserName(username);
		if (user == null) {
			return Result.badResult("用户名或密码错误");
		}
		if (user.getState() == User.STATE_OFF) {
			return Result.badResult("用户已锁定，请联系管理员解锁");
		}
		String pass = Md5.md5(password.getBytes());
		if (!user.getPassword().equalsIgnoreCase(pass)) {
			return Result.badResult("用户名或密码错误");
		}
		List<Resource> userResource = resourceService.getUserResources(user.getId());
		WebUtils.login(user, userResource);
		return Result.rightResult();
	}

}
