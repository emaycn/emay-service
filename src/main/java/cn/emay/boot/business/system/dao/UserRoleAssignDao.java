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

	void deleteByUserId(Long userId);

	List<UserRoleAssign> findByUserId(Long userId);
	
	List<UserRoleAssign> findByRoleId(Long roleId);
}
