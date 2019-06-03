package cn.emay.boot.dao.system;

import cn.emay.boot.pojo.system.UserRoleAssign;
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
