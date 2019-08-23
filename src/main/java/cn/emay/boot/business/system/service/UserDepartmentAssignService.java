package cn.emay.boot.business.system.service;

/**
 * 
 * @author lijunjian
 *
 */
public interface UserDepartmentAssignService {

	/*---------------------------------*/

	/**
	 * 查询部门下是否还有用户
	 */
	Long findByDepId(Long id);

}
