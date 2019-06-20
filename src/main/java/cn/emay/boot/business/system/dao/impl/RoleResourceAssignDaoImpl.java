package cn.emay.boot.business.system.dao.impl;

import org.springframework.stereotype.Repository;

import cn.emay.boot.base.dao.BaseSuperDaoImpl;
import cn.emay.boot.business.system.dao.RoleResourceAssignDao;
import cn.emay.boot.business.system.pojo.RoleResourceAssign;

/**
 * 
 * @author Frank
 *
 */
@Repository
public class RoleResourceAssignDaoImpl extends BaseSuperDaoImpl<RoleResourceAssign> implements RoleResourceAssignDao{

	@Override
	public void deleteByRoleId(Long roleId) {
		this.deleteByProperty("roleId", roleId);
	}


}
