package cn.emay.boot.business.system.service;

import java.util.List;

import cn.emay.boot.business.system.pojo.UserRoleAssign;


/**
 * 
* @项目名称：ebdp-web-operation 
* @类描述：用户角色   
* @创建人：lijunjian   
* @创建时间：2019年8月5日 下午7:16:08   
* @修改人：lijunjian   
* @修改时间：2019年8月5日 下午7:16:08   
* @修改备注：
 */
public interface UserRoleAssignService {

	/**
	 * 查询用户的所有角色
	 */
	List<UserRoleAssign> findByUserId(Long userId);
}
