package cn.emay.boot.restful.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.emay.boot.pojo.system.Role;
import cn.emay.boot.restful.common.WebAuth;
import cn.emay.boot.service.system.RoleService;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.Result;
import cn.emay.utils.result.SuperResult;

/**
 * 角色
 * 
 * @author 东旭
 *
 */
@RequestMapping(value = "/role/rest", method = RequestMethod.POST)
@RestController
public class RoleRestController {

	@Autowired
	private RoleService roleService;

	/**
	 * 角色列表
	 */
	@WebAuth({ "ROLE_VIEW" })
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
	@WebAuth({ "ROLE_MODIFY" })
	@RequestMapping("/modify")
	public Result modify(Long roleId, String roleName, String remark, Long[] resourceIds) {
		return roleService.modify(roleId, roleName, remark, resourceIds);
	}

	/**
	 * 添加角色
	 */
	@WebAuth({ "ROLE_ADD" })
	@RequestMapping("/add")
	public Result add(String roleName, String remark, Long[] resourceIds) {
		return roleService.add(roleName, remark, resourceIds);
	}

	/**
	 * 删除角色
	 */
	@WebAuth({ "ROLE_DELETE" })
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

}
