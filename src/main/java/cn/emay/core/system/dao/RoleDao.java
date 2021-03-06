package cn.emay.core.system.dao;

import cn.emay.core.system.pojo.Role;
import cn.emay.orm.BaseSuperDao;
import cn.emay.utils.db.common.Page;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author frank
 */
public interface RoleDao extends BaseSuperDao<Role> {

    /**
     * 分页查询角色
     *
     * @param start    起始条数
     * @param limit    查询多少条
     * @param roleName 角色名
     * @param roleType 类型
     * @return 分页数据
     */
    Page<Role> findPage(int start, int limit, String roleName, String roleType);

    /**
     * 查询角色名是否重复<br/>
     * 并且忽略传入的忽略角色ID
     *
     * @param roleName     角色名
     * @param ignoreRoleId 忽略对比的角色ID
     * @return 是否重复
     */
    boolean hasSameRoleName(String roleName, Long ignoreRoleId);

    /**
     * 根据用户ID批量查找用户的角色名字，并用,拼接
     *
     * @param userIds 用户IDs
     * @return 角色名字
     */
    Map<Long, String> findRoleNameByUserIds(Set<Long> userIds);

    List<Role> findUserRoles(Long userId);

}