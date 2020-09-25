package cn.emay.core.system.service;

import cn.emay.core.system.pojo.UserRoleAssign;

import java.util.List;

/**
 * @author lijunjian
 */
public interface UserRoleAssignService {

    /**
     * 查询角色的是否绑定用户
     *
     * @param roleId 角色ID
     * @return 是否绑定用户
     */
    Boolean findExistsByRoleId(Long roleId);

    /**
     * 查询用户的所有角色关联
     *
     * @param userId 用户ID
     * @return 用户的所有角色关联
     */
    List<UserRoleAssign> findByUserId(Long userId);

}
