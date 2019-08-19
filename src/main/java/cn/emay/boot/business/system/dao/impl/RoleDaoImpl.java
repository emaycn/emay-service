package cn.emay.boot.business.system.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.emay.boot.base.dao.BaseSuperDaoImpl;
import cn.emay.boot.business.system.dao.RoleDao;
import cn.emay.boot.business.system.pojo.Role;
import cn.emay.boot.utils.CheckUtil;
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
	public Page<Role> findPage(int start, int limit,String roleName) {
		Map<String,Object> params = new HashMap<String, Object>();
		String hql = FIND_ALL_HQL + " where isDelete=:isDelete";
		params.put("isDelete", false);
		if(!CheckUtil.isEmpty(roleName)){
			hql+=" and roleName like:roleName";
			params.put("roleName", "%"+CheckUtil.specialCodeEscape(roleName)+"%");
		}
		hql+=" order by createTime desc";
		return this.getPageResult(hql, start, limit, params, Role.class);
	}

	@Override
	public List<Role> findAllRole() {
		Map<String, Object> parms = new HashMap<String, Object>();
		String hql = "from Role where isDelete=:isDelete";
		parms.put("isDelete", false);
		return this.getListResult(Role.class, hql, parms);
	}
	
	@Override
	public Long countNumberByRoleName(String roleName, Long id) {
		Map<String, Object> parms = new HashMap<String, Object>();
		String hql = "select count(*) from Role where roleName=:roleName and isDelete!=:isDelete";
		parms.put("roleName", roleName);
		parms.put("isDelete", true);
		if (null!=id) {
			hql += " and id <> :id";
			parms.put("id", id);
		}
		return (Long) super.getUniqueResult(hql, parms);
	}

	@Override
	public void deleteById(Long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql =" update Role set isDelete = :isDelete where id=:id";
		params.put("isDelete", true);
		params.put("id", id);
		this.execByHql(hql, params);
	}
}