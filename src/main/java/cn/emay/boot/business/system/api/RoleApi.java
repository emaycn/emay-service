package cn.emay.boot.business.system.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.emay.boot.base.constant.ResourceEnum;
import cn.emay.boot.base.web.WebAuth;
import cn.emay.boot.business.system.pojo.Resource;
import cn.emay.boot.business.system.pojo.Role;
import cn.emay.boot.business.system.pojo.User;
import cn.emay.boot.business.system.service.ResourceService;
import cn.emay.boot.business.system.service.RoleService;
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
@RequestMapping(value = "/role", method = RequestMethod.POST)
@RestController
public class RoleApi {

	@Autowired
	private RoleService roleService;
	@Autowired
	private ResourceService resourceService;

	/**
	 * 角色列表
	 */
	@WebAuth({ ResourceEnum.ROLE_VIEW })
	@RequestMapping("/page")
	public SuperResult<Page<Role>> rolelist(int start, int limit) {
		Page<Role> userpage = roleService.findPage(start, limit);
		return SuperResult.rightResult(userpage);
	}

	/**
	 * 修改角色权限
	 * 
	 * @return
	 */
	@WebAuth({ ResourceEnum.ROLE_MODIFY })
	@RequestMapping("/modify")
	public Result modify(Long roleId, String roleName, String remark, Long[] resourceIds) {
		return roleService.modify(roleId, roleName, remark, resourceIds);
	}

	/**
	 * 添加角色
	 */
	@WebAuth({ ResourceEnum.ROLE_ADD })
	@RequestMapping("/add")
	public Result add(String roleName, String remark, Long[] resourceIds) {
		return roleService.add(roleName, remark, resourceIds);
	}

	/**
	 * 删除角色
	 */
	@WebAuth({ ResourceEnum.ROLE_DELETE })
	@RequestMapping("/delete")
	public Result delete(Long roleId) {
		if (roleId == null || roleId == 0l) {
			return Result.badResult("角色不存在");
		}
		if (roleId == 1l) {
			return Result.badResult("超管角色不能删除");
		}
		return roleService.delete(roleId);
	}
	
	/**
	 * 角色所有资源
	 */
	@WebAuth({ ResourceEnum.ROLE_VIEW })
	@RequestMapping(value = "/roleResource")
	public SuperResult<List<Resource>> roleResource(Long roleId) {
		List<Resource> userResource = resourceService.getRoleResources(roleId);
		return SuperResult.rightResult(userResource);
	}

	/**
	 * 系统所有角色
	 */
	@WebAuth({ ResourceEnum.ROLE_VIEW })
	@RequestMapping("/allRole")
	public SuperResult<List<Role>> allRoles() {
		List<Role> roles = roleService.findAll();
		return SuperResult.rightResult(roles);
	}

	/**
	 * 系统所有资源
	 */
	@WebAuth({ ResourceEnum.ROLE_VIEW })
	@RequestMapping(value = "/allResource")
	public SuperResult<List<Resource>> allresource() {
		List<Resource> allResource = resourceService.getAll();
		return SuperResult.rightResult(allResource);
	}
	
	/**
	 * 用戶所有角色
	 */
	@WebAuth({ ResourceEnum.ROLE_VIEW })
	@RequestMapping("/userRole")
	public SuperResult<List<Role>> userRoles(Long userId) {
		List<Role> roles = roleService.getUserRoles(userId);
		return SuperResult.rightResult(roles);
	}
	
	/**
	 * 当前用户所有资源
	 */
	@WebAuth({ ResourceEnum.ROLE_VIEW })
	@RequestMapping(value = "/myResource")
	public SuperResult<List<Resource>> userresource() {
		User user = WebUtils.getCurrentUser();
		List<Resource> userResource = resourceService.getUserResources(user.getId());
		return SuperResult.rightResult(userResource);
	}
	
}
