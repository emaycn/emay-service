package cn.emay.business.system.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import cn.emay.base.dao.BasePojoSuperDaoImpl;
import cn.emay.business.system.dao.RoleDao;
import cn.emay.business.system.pojo.Role;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.string.StringUtils;

/**
 * @author frank
 */
@Repository
public class RoleDaoImpl extends BasePojoSuperDaoImpl<Role> implements RoleDao {

	@Override
	public Page<Role> findPage(int start, int limit, String roleName, String roleType) {
		Map<String, Object> params = new HashMap<String, Object>(4);
		String hql = FIND_ALL_HQL + " where 1=1";
		if (!StringUtils.isEmpty(roleName)) {
			hql += " and roleName like:roleName";
			params.put("roleName", "%" + roleName + "%");
		}
		if (!StringUtils.isEmpty(roleType)) {
			hql += " and roleType like:roleType";
			params.put("roleType", "%" + roleType + "%");
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
			hql += " and id != :id";
			parms.put("id", ignoreRoleId);
		}
		return (Long) super.getUniqueResult(hql, parms) > 0;
	}

	@Override
	public Map<Long, String> findRoleNameByUserIds(Set<Long> userIds) {
		String hql = "select r.roleName , ur.userId from Role r , UserRoleAssign ur where r.id = ur.roleId and ur.userId in (:userIds) ";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userIds", userIds);
		List<Object[]> list = this.getListResult(Object[].class, hql, param);
		Map<Long, String> maps = new HashMap<>();
		for (Object[] oba : list) {
			String name = (String) oba[0];
			Long id = (Long) oba[1];
			if (maps.containsKey(id)) {
				maps.put(id, maps.get(id) + " , " + name);
			} else {
				maps.put(id, name);
			}
		}
		return maps;
	}
	
	@Override
	public List<Role> findUserRoles(Long userId) {
		String hql = "select r from UserRoleAssign ura , Role r where ura.roleId = r.id and ura.userId = :userId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		List<Role> roles = this.getListResult(Role.class, hql, params);
		return roles;
	}

}