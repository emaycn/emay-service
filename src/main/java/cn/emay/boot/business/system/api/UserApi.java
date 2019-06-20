package cn.emay.boot.business.system.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.emay.boot.base.web.WebAuth;
import cn.emay.boot.business.system.dto.UserDTO;
import cn.emay.boot.business.system.pojo.User;
import cn.emay.boot.business.system.service.UserService;
import cn.emay.boot.utils.WebUtils;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.Result;
import cn.emay.utils.result.SuperResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 角色
 * 
 * @author 东旭
 *
 */
@RestController
@RequestMapping(value = "/user", method = RequestMethod.POST)
@Api(description = "角色管理")
public class UserApi {

	@Autowired
	private UserService userService;

	/**
	 * 用户列表
	 */
	@WebAuth({ "USER_VIEW" })
	@RequestMapping("/page")
	@ApiOperation("分页查询用户列表")
	public SuperResult<Page<UserDTO>> list(@ApiParam("起始数据位置") int start, @ApiParam("数据条数") int limit, @ApiParam("用户状态") int state, @ApiParam("用户名") String username) {
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
