package cn.emay.boot.business.system.dao;

import cn.emay.boot.business.system.pojo.RoleResourceAssign;
import cn.emay.orm.BaseSuperDao;

/**
 * 
 * @author Frank
 *
 */
public interface RoleResourceAssignDao extends BaseSuperDao<RoleResourceAssign> {

	/**
	 * 删除角色的绑定关系
	 * 
	 * @param roleId
	 *            角色ID
	 */
	void deleteByRoleId(Long roleId);

}
