package cn.emay.boot.business.system.service;

import java.util.List;

import cn.emay.boot.business.system.pojo.UserRoleAssign;

/**
 * 
 * @author lijunjian
 *
 */
public interface UserRoleAssignService {

	/**
	 * 查询用户的所有角色
	 */
	List<UserRoleAssign> findByUserId(Long userId);

	/**
	 * 查询角色的是否绑定用户
	 */
	Boolean findByRoleId(Long roleId);
}
