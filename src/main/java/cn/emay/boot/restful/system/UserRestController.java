package cn.emay.boot.restful.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.emay.boot.dto.system.UserDTO;
import cn.emay.boot.pojo.system.User;
import cn.emay.boot.restful.WebAuth;
import cn.emay.boot.service.system.UserService;
import cn.emay.boot.utils.WebUtils;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.Result;
import cn.emay.utils.result.SuperResult;

/**
 * 角色
 * 
 * @author 东旭
 *
 */
@RestController
@RequestMapping(value = "/user/rest", method = RequestMethod.POST)
public class UserRestController {

	@Autowired
	private UserService userService;

	/**
	 * 用户列表
	 */
	@WebAuth({ "USER_VIEW" })
	@RequestMapping("/page")
	public SuperResult<Page<UserDTO>> list(int start, int limit, int state, String username) {
		Page<UserDTO> userpage = userService.findPage(start, limit, username, state);
		return SuperResult.rightResult(userpage);
	}

	/**
	 * 用户详细信息
	 */
	@WebAuth({ "USER_VIEW" })
	@RequestMapping("/info")
	public SuperResult<UserDTO> userinfo(Long userId) {
		if (userId == null || userId == 0l) {
			return SuperResult.badResult("用户不存在");
		}
		User user = userService.findById(userId);
		if (user == null) {
			return SuperResult.badResult("用户不存在");
		}
		UserDTO dto = new UserDTO(user);
		return SuperResult.rightResult(dto);
	}

	/**
	 * 修改用户
	 */
	@WebAuth({ "USER_MODIFY" })
	@RequestMapping("/modify")
	public Result modify(String realname, String remark, String email, String mobile, Long[] roleIds, Long userId) {
		return userService.modify(userId, realname, mobile, email, remark, roleIds);
	}

	/**
	 * 添加用户
	 */
	@WebAuth({ "USER_ADD" })
	@RequestMapping("/add")
	public Result add(String username, String password, String realname, String remark, String email, String mobile, Long[] roleIds) {
		User currentUser = WebUtils.getCurrentUser();
		return userService.add(username, password, realname, mobile, email, remark, currentUser.getId(), roleIds);
	}

	/**
	 * 删除用户
	 */
	@WebAuth({ "USER_DELETE" })
	@RequestMapping("/delete")
	public Result delete(Long userId) {
		if (userId == null || userId == 0l) {
			return Result.badResult("用户不存在");
		}
		if (userId == 1l) {
			return Result.badResult("不能操作ADMIN");
		}
		return userService.delete(userId);
	}

	/**
	 * 启用用户
	 */
	@WebAuth({ "USER_OPER" })
	@RequestMapping("/on")
	public Result on(Long userId) {
		if (userId == null || userId == 0l) {
			return Result.badResult("用户不存在");
		}
		if (userId == 1l) {
			return Result.badResult("不能操作ADMIN");
		}
		return userService.on(userId);
	}

	/**
	 * 停用用户
	 */
	@WebAuth({ "USER_OPER" })
	@RequestMapping("/off")
	public Result off(Long userId) {
		if (userId == null || userId == 0l) {
			return Result.badResult("用户不存在");
		}
		if (userId == 1l) {
			return Result.badResult("不能操作ADMIN");
		}
		return userService.off(userId);
	}

}
