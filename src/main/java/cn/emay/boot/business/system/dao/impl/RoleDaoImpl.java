package cn.emay.boot.business.system.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.emay.boot.base.dao.BasePojoSuperDaoImpl;
import cn.emay.boot.business.system.dao.RoleDao;
import cn.emay.boot.business.system.pojo.Role;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.string.StringUtils;

/**
 * @author frank
 */
@Repository
public class RoleDaoImpl extends BasePojoSuperDaoImpl<Role> implements RoleDao {

	@Override
	public Page<Role> findPage(int start, int limit, String roleName) {
		Map<String, Object> params = new HashMap<String, Object>(4);
		String hql = FIND_ALL_HQL + " where 1=1";
		if (!StringUtils.isEmpty(roleName)) {
			hql += " and roleName like:roleName";
			params.put("roleName", "%" + roleName + "%");
		}
		hql += " order by createTime desc";
		return this.getPageResult(hql, start, limit, params, Role.class);
	}
	
	@Override
	public boolean hasSameRoleName(String roleName, Long ignoreRoleId) {
		Map<String, Object> parms = new HashMap<String, Object>(4);
		String hql = "select count(*) from Role where roleName=:roleName ";
		parms.put("roleName", roleName);
		if (null != ignoreRoleId) {
			hql += " and id <> :id";
			parms.put("id", ignoreRoleId);
		}
		return (Long) super.getUniqueResult(hql, parms) > 0;
	}
	
	/*---------------------------------*/
	
	@Override
	public List<Role> getUserRoles(Long userId) {
		String hql = " select r from UserRoleAssign ura , Role r where r.id = ura.roleId and ura.userId = :userId ";
		Map<String, Object> params = new HashMap<String, Object>(4);
		params.put("userId", userId);
		return this.getListResult(Role.class, hql, params);
	}

	

	@Override
	public List<Role> findAllRole() {
		Map<String, Object> parms = new HashMap<String, Object>(4);
		String hql = "from Role where isDelete=:isDelete";
		parms.put("isDelete", false);
		return this.getListResult(Role.class, hql, parms);
	}

	
}