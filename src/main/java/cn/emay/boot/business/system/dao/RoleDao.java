package cn.emay.boot.business.system.dao;

import java.util.List;

import cn.emay.boot.business.system.pojo.Role;
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
	 * 分页查询
	 */
	Page<Role> findPage(int start, int limit,String roleName);
	
	List<Role> findAllRole();
	
	Long countNumberByRoleName(String roleName, Long id);
	
	void deleteById(Long id);
}