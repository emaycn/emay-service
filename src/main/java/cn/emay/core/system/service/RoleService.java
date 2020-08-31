package cn.emay.core.system.service;

import cn.emay.core.system.pojo.Role;
import cn.emay.core.system.pojo.RoleResourceAssign;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.Result;

import java.util.List;

/**
 * @author Frank
 */
public interface RoleService {

    /**
     * 分页查询角色
     *
     * @param start    起始条数
     * @param limit    查询多少条
     * @param roleName 角色名
     * @param roleType
     * @return
     */
    Page<Role> findPage(int start, int limit, String roleName, String roleType);

    /**
     * 按照ID查找角色
     *
     * @param roleId 角色ID
     * @return
     */
    Role findById(Long roleId);

    /**
     * 是否有存在的角色名<br/>
     * 并且忽略传入的忽略角色ID
     *
     * @param roleName     角色名
     * @param ignoreRoleId 忽略对比的角色ID
     * @return
     */
    boolean hasSameRoleName(String roleName, Long ignoreRoleId);

    /**
     * 保存角色及其资源
     *
     * @param roleName               角色名
     * @param remark                 备注
     * @param roleResourceAssignList 关联资源
     * @param roleType
     * @return
     */
    Result add(String roleName, String remark, List<RoleResourceAssign> roleResourceAssignList, String roleType);

    /**
     * 更新角色及其资源
     *
     * @param roleId                 角色ID
     * @param roleName               角色名
     * @param remark                 备注
     * @param roleResourceAssignList 关联资源
     * @return
     */
    Result modify(Long roleId, String roleName, String remark, List<RoleResourceAssign> roleResourceAssignList);

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     * @return
     */
    Result delete(Long roleId);

    /**
     * 获取所有角色
     *
     * @return
     */
    List<Role> findAll();

    List<Role> findUserRoles(Long userId);

}
