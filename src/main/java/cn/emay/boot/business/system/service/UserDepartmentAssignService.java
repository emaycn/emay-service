package cn.emay.boot.business.system.service;

import cn.emay.boot.business.system.pojo.UserDepartmentAssign;


/**
 * 
* @项目名称：ebdp-web-operation 
* @类描述：用户部门   
* @创建人：lijunjian   
* @创建时间：2019年7月30日 上午11:08:30   
* @修改人：lijunjian   
* @修改时间：2019年7月30日 上午11:08:30   
* @修改备注：
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
