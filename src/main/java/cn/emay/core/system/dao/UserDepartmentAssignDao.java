package cn.emay.core.system.dao;

import cn.emay.core.system.pojo.UserDepartmentAssign;
import cn.emay.orm.BaseSuperDao;

/**
 * @author Frank
 */
public interface UserDepartmentAssignDao extends BaseSuperDao<UserDepartmentAssign> {

    /**
     * 删除用户相关的部门关联
     *
     * @param userId 用户ID
     */
    void deleteByUserId(Long userId);

    /**
     * 查询部门下用户数量
     *
     * @param departmentId 部门ID
     * @return 数量
     */
    Long findByDepId(Long departmentId);

}
