package cn.emay.boot.business.system.dao;

import java.util.List;

import cn.emay.boot.business.system.pojo.UserRoleAssign;
import cn.emay.orm.BaseSuperDao;

/**
 * 
 * @author Frank
 *
 */
public interface UserRoleAssignDao extends BaseSuperDao<UserRoleAssign> {

	/**
	 * 根据角色ID查询关联关系
	 * 
	 * @param roleId 角色ID
	 * @return
	 */
	List<UserRoleAssign> findByRoleId(Long roleId);

	/*---------------------------------*/

	void deleteByUserId(Long userId);

	List<UserRoleAssign> findByUserId(Long userId);

}
