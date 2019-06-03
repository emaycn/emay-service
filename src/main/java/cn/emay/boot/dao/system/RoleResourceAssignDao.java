package cn.emay.boot.dao.system;

import cn.emay.boot.pojo.system.RoleResourceAssign;
import cn.emay.orm.BaseSuperDao;

/**
 * 
 * @author Frank
 *
 */
public interface RoleResourceAssignDao extends BaseSuperDao<RoleResourceAssign> {

	/**
	 * 删除角色的绑定关系
	 */
	void deleteByRoleId(Long roleId);

}
