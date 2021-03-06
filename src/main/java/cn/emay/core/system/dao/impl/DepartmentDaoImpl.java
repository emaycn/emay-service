package cn.emay.core.system.dao.impl;

import cn.emay.configuration.db.BasePojoSuperDaoImpl;
import cn.emay.core.system.dao.DepartmentDao;
import cn.emay.core.system.pojo.Department;
import cn.emay.utils.db.common.Page;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lijunjian
 */
@Repository
public class DepartmentDaoImpl extends BasePojoSuperDaoImpl<Department> implements DepartmentDao {

    @Override
    public Map<Long, String> findDepartmentNameByUserIds(Set<Long> userIds) {
        String hql = "select d.departmentName , ud.systemUserId from Department d , UserDepartmentAssign ud where d.id = ud.systemDepartmentId and ud.systemUserId in (:userIds) ";
        Map<String, Object> param = new HashMap<>();
        param.put("userIds", userIds);
        List<Object[]> list = this.getListResult(Object[].class, hql, param);
        Map<Long, String> maps = new HashMap<>();
        for (Object[] oba : list) {
            String name = (String) oba[0];
            Long id = (Long) oba[1];
            maps.put(id, name);
        }
        return maps;
    }

    @Override
    public List<Department> findByParentId(Long parentId) {
        return this.findListByProperty("parentDepartmentId", parentId);
    }

    @Override
    public Department findByUserId(Long userId) {
        String hql = "select d from Department d , UserDepartmentAssign ud where d.id = ud.systemDepartmentId and ud.systemUserId = :userId ";
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        return this.getUniqueResult(Department.class, hql, param);
    }

    @Override
    public Page<Department> findPage(Long id, String departmentName, int start, int limit) {
        Map<String, Object> param = new HashMap<>();
        String hql = "from Department where  parentDepartmentId =:id ";
        param.put("id", id);
        if (!StringUtils.isEmpty(departmentName)) {
            hql = hql + " and departmentName like:departmentName";
            param.put("departmentName", "%" + departmentName + "%");
        }
        hql += " order by id desc";
        return this.getPageResult(hql, start, limit, param, Department.class);
    }

    @Override
    public List<Department> findByIds(List<Long> ids) {
        Map<String, Object> param = new HashMap<>();
        String hql = "from Department where  id in(:ids)";
        param.put("ids", ids);
        return this.getListResult(Department.class, hql, param);
    }

    @Override
    public Boolean hasSameDepartmentNameAtParent(String departmentName, Long parentId, Long ignoreId) {
        Map<String, Object> param = new HashMap<>();
        String hql = "SELECT count(*) from Department where departmentName=:departmentName and parentDepartmentId = :parentId ";
        param.put("departmentName", departmentName);
        param.put("parentId", parentId);
        if (ignoreId != null) {
            hql = hql + " and id <>:id";
            param.put("id", ignoreId);
        }
        return (Long) super.getUniqueResult(hql, param) > 0;
    }

    @Override
    public Long findCountByParentId(Long parentId) {
        Map<String, Object> param = new HashMap<>();
        String hql = "select count(*) from Department where  parentDepartmentId = :parentId";
        param.put("parentId", parentId);
        return (Long) super.getUniqueResult(hql, param);
    }

}
