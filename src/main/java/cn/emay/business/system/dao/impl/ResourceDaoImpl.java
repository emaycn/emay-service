package cn.emay.business.system.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.emay.base.dao.BasePojoSuperDaoImpl;
import cn.emay.business.system.dao.ResourceDao;
import cn.emay.business.system.pojo.Resource;

/**
 * 
 * @author Frank
 *
 */
@Repository
public class ResourceDaoImpl extends BasePojoSuperDaoImpl<Resource> implements ResourceDao {

	@Override
	public List<Resource> getRoleResources(Long roleId) {
		String hql = "select re from RoleResourceAssign rra , Resource re where  rra.roleId = :roleId  and re.id = rra.resourceId  ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleId", roleId);
		return this.getListResult(Resource.class, hql, params);
	}

	@Override
	public List<Resource> getUserResources(Long userId) {
		String hql = "select re from Resource re , RoleResourceAssign rra , Role r , UserRoleAssign ura where re.id = rra.resourceId and rra.roleId = r.id and r.id = ura.roleId and ura.userId = :userId ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		return this.getListResult(Resource.class, hql, params);
	}
	
	@Override
	public List<Resource> getByResourceType(String resourceType){
		return this.findListByProperty("resourceType", resourceType);
	}

}