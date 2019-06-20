package cn.emay.boot.business.system.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.emay.boot.base.dao.BaseSuperDaoImpl;
import cn.emay.boot.business.system.dao.RoleDao;
import cn.emay.boot.business.system.pojo.Role;
import cn.emay.utils.db.common.Page;

/**
 * @author frank
 */
@Repository
public class RoleDaoImpl extends BaseSuperDaoImpl<Role> implements RoleDao {

	@Override
	public List<Role> getUserRoles(Long userId) {
		String hql = " select r from UserRoleAssign ura , Role r where r.id = ura.roleId and ura.userId = :userId ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		return this.getListResult(Role.class, hql, params);
	}

	@Override
	public Role findByRoleName(String roleName) {
		return this.findByProperty("roleName", roleName);
	}

	@Override
	public Page<Role> findPage(int start, int limit) {
		String hql = FIND_ALL_HQL + " where isDelete != true order by id asc";
		return this.getPageResult(hql, start, limit, null, Role.class);
	}

}