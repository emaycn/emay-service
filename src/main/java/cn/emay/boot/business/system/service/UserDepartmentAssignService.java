package cn.emay.boot.business.system.service;

/**
 * 
 * @author lijunjian
 *
 */
public interface UserDepartmentAssignService {

	/**
	 * 查询部门下用户数量
	 * 
	 * @param departmentId
	 *            部门ID
	 * @return
	 */
	Long findByDepId(Long departmentId);

}
