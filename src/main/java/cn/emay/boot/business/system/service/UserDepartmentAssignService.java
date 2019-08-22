package cn.emay.boot.business.system.service;

import cn.emay.boot.business.system.pojo.UserDepartmentAssign;

/**
 * 
 * @author lijunjian
 *
 */
public interface UserDepartmentAssignService {

	/**
	 * 查询部门下是否还有用户
	 */
	Long findByDepId(Long id);
	
	/**
	 * 查询用户所属的部门
	 * 
	 * @param userId
	 * @return
	 */
	UserDepartmentAssign findByUserId(Long userId);
}
