package cn.emay.boot.business.system.service;

import java.util.List;

import cn.emay.boot.business.system.pojo.Role;
import cn.emay.boot.business.system.pojo.RoleResourceAssign;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.Result;

/**
 * 
 * @author Frank
 *
 */
public interface RoleService {

	/**
	 * 保存角色及其资源
	 */
	public Result add(String roleName, String remark, List<RoleResourceAssign> roleList);

	/**
	 * 更新角色及其资源
	 */
	public Result modify(Long roleId, String roleName, String remark, List<RoleResourceAssign> roleList);

	/**
	 * 删除角色
	 */
	public Result delete(Long roleId);

	/**
	 * 按照ID查找角色
	 */
	public Role findById(Long roleId);

	/**
	 * 分页查询角色
	 */
	public Page<Role> findPage(int start, int limit, String roleName);

	/**
	 * 获取用户的所有角色
	 */
	public List<Role> getUserRoles(Long userId);

	/**
	 * 获取所有角色
	 */
	public List<Role> findAll();

	/**
	 * 查询用户名是否重复
	 */
	Long countNumberByRoleName(String roleName, Long id);

}
