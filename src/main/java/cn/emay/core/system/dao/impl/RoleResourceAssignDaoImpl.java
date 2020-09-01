package cn.emay.core.system.dao.impl;

import cn.emay.configuration.db.BasePojoSuperDaoImpl;
import cn.emay.core.system.dao.RoleResourceAssignDao;
import cn.emay.core.system.pojo.RoleResourceAssign;
import org.springframework.stereotype.Repository;

/**
 * @author Frank
 */
@Repository
public class RoleResourceAssignDaoImpl extends BasePojoSuperDaoImpl<RoleResourceAssign> implements RoleResourceAssignDao {

    @Override
    public void deleteByRoleId(Long roleId) {
        this.deleteByProperty("roleId", roleId);
    }

}
