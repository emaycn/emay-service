package cn.emay.boot.dao.system;

import java.util.List;

import cn.emay.boot.pojo.system.Role;
import cn.emay.orm.BaseSuperDao;
import cn.emay.utils.db.common.Page;

/**
 * @author frank
 */
public interface RoleDao extends BaseSuperDao<Role> {

	/**
	 * 获取用户的所有角色
	 */
	List<Role> getUserRoles(Long userId);

	/**
	 * 根据角色名查询
	 */
	Role findByRoleName(String roleName);

	/**
	 * 分页查询
	 */
	Page<Role> findPage(int start, int limit);
}