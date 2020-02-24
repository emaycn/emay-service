package cn.emay.business.system.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.emay.base.dao.BasePojoSuperDaoImpl;
import cn.emay.business.system.dao.UserRoleAssignDao;
import cn.emay.business.system.pojo.UserRoleAssign;

/**
 * 
 * @author Frank
 *
 */
@Repository
public class UserRoleAssignDaoImpl extends BasePojoSuperDaoImpl<UserRoleAssign> implements UserRoleAssignDao {

	@Override
	public List<UserRoleAssign> findByRoleId(Long roleId) {
		return this.findListByProperty("roleId", roleId);
	}

	@Override
	public void deleteByUserId(Long userId) {
		this.deleteByProperty("userId", userId);
	}

	@Override
	public List<UserRoleAssign> findByUserId(Long userId) {
		return this.findListByProperty("userId", userId);
	}

}
