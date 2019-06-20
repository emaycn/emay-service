package cn.emay.boot.business.system.dao;

import cn.emay.boot.business.system.pojo.UserRoleAssign;
import cn.emay.orm.BaseSuperDao;

/**
 * 
 * @author Frank
 *
 */
public interface UserRoleAssignDao extends BaseSuperDao<UserRoleAssign> {

	/**
	 * 根据用户id删除关联关系
	 * 
	 * @param userId
	 */
	void deleteByUserId(Long userId);

}
