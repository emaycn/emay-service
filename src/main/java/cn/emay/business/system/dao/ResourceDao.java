package cn.emay.business.system.dao;

import java.util.List;

import cn.emay.business.system.pojo.Resource;
import cn.emay.orm.BaseSuperDao;

/**
 * 
 * @author Frank
 *
 */
public interface ResourceDao extends BaseSuperDao<Resource> {

	/**
	 * 获取角色所有资源
	 * 
	 * @param roleId
	 *            角色ID
	 * @return
	 */
	List<Resource> getRoleResources(Long roleId);

	/**
	 * 获取用户所有的资源
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	List<Resource> getUserResources(Long userId);

	List<Resource> getByResourceType(String resourceType);

}
