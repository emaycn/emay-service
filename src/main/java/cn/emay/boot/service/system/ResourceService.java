package cn.emay.boot.service.system;

import java.util.List;

import cn.emay.boot.pojo.system.Resource;

/**
 * 
 * @author Frank
 *
 */
public interface ResourceService {

	/**
	 * 获取所有资源
	 */
	public List<Resource> getAll();

	/**
	 * 获取用户资源
	 */
	public List<Resource> getUserResources(Long userId);

	/**
	 * 获取角色所有资源
	 */
	public List<Resource> getRoleResources(Long roleId);

}
