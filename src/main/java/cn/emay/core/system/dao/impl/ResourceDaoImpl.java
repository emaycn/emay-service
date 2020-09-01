package cn.emay.core.system.dao.impl;

import cn.emay.configuration.db.BasePojoSuperDaoImpl;
import cn.emay.core.system.dao.ResourceDao;
import cn.emay.core.system.pojo.Resource;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Frank
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
    public List<Resource> getByResourceType(String resourceType) {
        return this.findListByProperty("resourceType", resourceType);
    }

}
