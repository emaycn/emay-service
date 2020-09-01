package cn.emay.core.system.dao.impl;

import cn.emay.configuration.db.BasePojoSuperDaoImpl;
import cn.emay.core.system.dao.UserDepartmentAssignDao;
import cn.emay.core.system.pojo.UserDepartmentAssign;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Frank
 */
@Repository
public class UserDepartmentAssignDaoImpl extends BasePojoSuperDaoImpl<UserDepartmentAssign> implements UserDepartmentAssignDao {

    @Override
    public void deleteByUserId(Long userId) {
        this.deleteByProperty("systemUserId", userId);
    }

    @Override
    public Long findByDepId(Long departmentId) {
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = "select count(*) from UserDepartmentAssign where systemDepartmentId=:id";
        params.put("id", departmentId);
        return (Long) super.getUniqueResult(hql, params);
    }

}
