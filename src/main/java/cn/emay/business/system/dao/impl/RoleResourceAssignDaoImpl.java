package cn.emay.business.system.dao.impl;

import org.springframework.stereotype.Repository;

import cn.emay.base.dao.BasePojoSuperDaoImpl;
import cn.emay.business.system.dao.RoleResourceAssignDao;
import cn.emay.business.system.pojo.RoleResourceAssign;

/**
 * 
 * @author Frank
 *
 */
@Repository
public class RoleResourceAssignDaoImpl extends BasePojoSuperDaoImpl<RoleResourceAssign> implements RoleResourceAssignDao {

	@Override
	public void deleteByRoleId(Long roleId) {
		this.deleteByProperty("roleId", roleId);
	}

}
