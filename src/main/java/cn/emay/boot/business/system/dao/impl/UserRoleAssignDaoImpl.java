package cn.emay.boot.business.system.dao.impl;

import org.springframework.stereotype.Repository;

import cn.emay.boot.base.dao.BaseSuperDaoImpl;
import cn.emay.boot.business.system.dao.UserRoleAssignDao;
import cn.emay.boot.business.system.pojo.UserRoleAssign;

/**
 * 
 * @author Frank
 *
 */
@Repository
public class UserRoleAssignDaoImpl extends BaseSuperDaoImpl<UserRoleAssign> implements UserRoleAssignDao {

	@Override
	public void deleteByUserId(Long userId) {
		this.deleteByProperty("userId", userId);
	}

}
