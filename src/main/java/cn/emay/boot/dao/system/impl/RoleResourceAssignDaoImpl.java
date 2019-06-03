package cn.emay.boot.dao.system.impl;

import org.springframework.stereotype.Repository;

import cn.emay.boot.dao.base.BaseSuperDaoImpl;
import cn.emay.boot.dao.system.RoleResourceAssignDao;
import cn.emay.boot.pojo.system.RoleResourceAssign;

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
