package cn.emay.core.system.dao;

import cn.emay.core.system.pojo.UserRoleAssign;
import cn.emay.orm.BaseSuperDao;

import java.util.List;

/**
 * @author Frank
 */
public interface UserRoleAssignDao extends BaseSuperDao<UserRoleAssign> {

    /**
     * 根据角色ID查询关联关系
     *
     * @param roleId 角色ID
     * @return
     */
    List<UserRoleAssign> findByRoleId(Long roleId);

    /**
     * 删除用户的所有角色关联
     *
     * @param userId 用户ID
     */
    void deleteByUserId(Long userId);

    /**
     * 查询用户的所有角色关联
     *
     * @param userId 用户ID
     * @return
     */
    List<UserRoleAssign> findByUserId(Long userId);

}
