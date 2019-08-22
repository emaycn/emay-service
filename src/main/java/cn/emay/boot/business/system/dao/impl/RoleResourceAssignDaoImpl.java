package cn.emay.boot.business.system.dao.impl;

import org.springframework.stereotype.Repository;

import cn.emay.boot.base.dao.BasePojoSuperDaoImpl;
import cn.emay.boot.business.system.dao.RoleResourceAssignDao;
import cn.emay.boot.business.system.pojo.RoleResourceAssign;

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
