package cn.emay.boot.dao.system.impl;

import org.springframework.stereotype.Repository;

import cn.emay.boot.dao.base.BaseSuperDaoImpl;
import cn.emay.boot.dao.system.UserRoleAssignDao;
import cn.emay.boot.pojo.system.UserRoleAssign;

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
