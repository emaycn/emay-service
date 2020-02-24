package cn.emay.business.system.service;

import java.util.List;

import cn.emay.business.system.pojo.UserRoleAssign;

/**
 * 
 * @author lijunjian
 *
 */
public interface UserRoleAssignService {

	/**
	 * 查询角色的是否绑定用户
	 * 
	 * @param roleId
	 *            角色ID
	 * @return
	 */
	Boolean findExistsByRoleId(Long roleId);

	/**
	 * 查询用户的所有角色关联
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	List<UserRoleAssign> findByUserId(Long userId);

}
