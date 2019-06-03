package cn.emay.boot.dao.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.emay.boot.dao.base.BaseSuperDaoImpl;
import cn.emay.boot.dao.system.ResourceDao;
import cn.emay.boot.pojo.system.Resource;

/**
 * 
 * @author Frank
 *
 */
@Repository
public class ResourceDaoImpl extends BaseSuperDaoImpl<Resource> implements ResourceDao {

	@Override
	public List<Resource> getUserResources(Long userId) {
		String hql = "select re from Resource re , RoleResourceAssign rra , Role r , UserRoleAssign ura where re.id = rra.resourceId and rra.roleId = r.id and r.id = ura.roleId and ura.userId = :userId ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		return this.getListResult(Resource.class, hql, params);
	}

	@Override
	public List<Resource> getRoleResources(Long roleId) {
		String hql = "select re from Resource re , RoleResourceAssign rra where re.id = rra.resourceId and rra.roleId = :roleId ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleId", roleId);
		return this.getListResult(Resource.class, hql, params);
	}

}
