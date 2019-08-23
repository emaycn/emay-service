package cn.emay.boot.business.system.service;

import java.util.List;

import cn.emay.boot.business.system.pojo.Resource;

/**
 * 
 * @author lijunjian
 *
 */
public interface ResourceService {

	/**
	 * 获取所有资源
	 * 
	 * @return
	 */
	List<Resource> getAll();

	/**
	 * 获取角色所有资源
	 * 
	 * @param roleId
	 *            角色ID
	 * @return
	 */
	List<Resource> getRoleResources(Long roleId);

	/*---------------------------------*/

	/**
	 * 获取用户资源
	 */
	List<Resource> getUserResources(Long userId);

}
