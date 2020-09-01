package cn.emay.core.system.dao.impl;

import cn.emay.configuration.db.BasePojoSuperDaoImpl;
import cn.emay.core.system.dao.UserRoleAssignDao;
import cn.emay.core.system.pojo.UserRoleAssign;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Frank
 */
@Repository
public class UserRoleAssignDaoImpl extends BasePojoSuperDaoImpl<UserRoleAssign> implements UserRoleAssignDao {

    @Override
    public List<UserRoleAssign> findByRoleId(Long roleId) {
        return this.findListByProperty("roleId", roleId);
    }

    @Override
    public void deleteByUserId(Long userId) {
        this.deleteByProperty("userId", userId);
    }

    @Override
    public List<UserRoleAssign> findByUserId(Long userId) {
        return this.findListByProperty("userId", userId);
    }

}
