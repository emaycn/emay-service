package cn.emay.boot.business.system.dao;

import cn.emay.boot.business.system.pojo.UserDepartmentAssign;
import cn.emay.orm.BaseSuperDao;

/**
 * 
 * @author Frank
 *
 */
public interface UserDepartmentAssignDao extends BaseSuperDao<UserDepartmentAssign> {

	/**
	 * 删除用户相关的部门关联
	 * 
	 * @param userId
	 *            用户ID
	 */
	void deleteByUserId(Long userId);

	/*---------------------------------*/

	Long findByDepId(Long id);

}
