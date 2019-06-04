package cn.emay.boot.web.restful.system;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.emay.boot.pojo.system.Resource;
import cn.emay.boot.pojo.system.Role;
import cn.emay.boot.pojo.system.User;
import cn.emay.boot.service.system.ResourceService;
import cn.emay.boot.service.system.RoleService;
import cn.emay.boot.service.system.UserService;
import cn.emay.boot.web.auth.WebAuth;
import cn.emay.boot.web.utils.WebUtils;
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
@RequestMapping(value = "/rest", method = RequestMethod.POST)
public class SystemRestController {

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
