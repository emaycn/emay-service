package cn.emay.boot.business.system.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.emay.boot.business.system.pojo.Role;
import cn.emay.orm.BaseSuperDao;
import cn.emay.utils.db.common.Page;

/**
 * @author frank
 */
public interface RoleDao extends BaseSuperDao<Role> {

	/**
	 * 分页查询角色
	 * 
	 * @param start
	 *            起始条数
	 * @param limit
	 *            查询多少条
	 * @param roleName
	 *            角色名
	 * @return
	 */
	Page<Role> findPage(int start, int limit, String roleName);

	/**
	 * 查询角色名是否重复<br/>
	 * 并且忽略传入的忽略角色ID
	 * 
	 * @param roleName
	 *            角色名
	 * @param ignoreRoleId
	 *            忽略对比的角色ID
	 * @return
	 */
	boolean hasSameRoleName(String roleName, Long ignoreRoleId);

	/**
	 * 根据用户ID批量查找用户的角色名字，并用,拼接
	 * 
	 * @param userIds
	 *            用户IDs
	 * @return
	 */
	Map<Long, String> findRoleNameByUserIds(Set<Long> userIds);

	/*---------------------------------*/

	/**
	 * 获取用户的所有角色
	 */
	List<Role> getUserRoles(Long userId);

}