package cn.emay.boot.business.system.api;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.emay.boot.base.web.WebAuth;
import cn.emay.boot.business.system.pojo.Resource;
import cn.emay.boot.business.system.pojo.Role;
import cn.emay.boot.business.system.pojo.User;
import cn.emay.boot.business.system.service.ResourceService;
import cn.emay.boot.business.system.service.RoleService;
import cn.emay.boot.business.system.service.UserService;
import cn.emay.boot.utils.WebUtils;
import cn.emay.utils.encryption.Md5;
import cn.emay.utils.result.Result;
import cn.emay.utils.result.SuperResult;

/**
 * 根
 * 
 * @author 东旭
 *
 */
@RestController
@RequestMapping(method = RequestMethod.POST)
public class SystemApi {

	@Autowired
	private UserService userService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private RoleService roleService;

	/**
	 * 系统所有资源
	 */
	@WebAuth
	@RequestMapping(value = "/allResource")
	public SuperResult<List<Resource>> allresource() {
		List<Resource> allResource = resourceService.getAll();
		return SuperResult.rightResult(allResource);
	}

	/**
	 * 用户所有资源
	 */
	@WebAuth
	@RequestMapping(value = "/userResource")
	public SuperResult<List<Resource>> userresource(Long userId) {
		List<Resource> userResource = resourceService.getUserResources(userId);
		return SuperResult.rightResult(userResource);
	}

	/**
	 * 角色所有资源
	 */
	@WebAuth
	@RequestMapping(value = "/roleResource")
	public SuperResult<List<Resource>> roleResource(Long roleId) {
		List<Resource> userResource = resourceService.getRoleResources(roleId);
		return SuperResult.rightResult(userResource);
	}

	/**
	 * 系统所有角色
	 */
	@WebAuth
	@RequestMapping("/allRole")
	public SuperResult<List<Role>> allRoles() {
		List<Role> roles = roleService.findAll();
		return SuperResult.rightResult(roles);
	}

	/**
	 * 用戶所有角色
	 */
	@WebAuth
	@RequestMapping("/userRole")
	public SuperResult<List<Role>> userRoles(Long userId) {
		List<Role> roles = roleService.getUserRoles(userId);
		return SuperResult.rightResult(roles);
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
		if (user == null) {
			WebUtils.toNoLogin();
			return Result.badResult();
		}
		String pass = Md5.md5(password.getBytes());
		if (!user.getPassword().equals(pass)) {
			return Result.badResult("密码错误");
		}
		userService.modifyPassword(user.getId(), newpass);
		return Result.rightResult();
	}

}
