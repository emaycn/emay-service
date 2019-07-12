package cn.emay.boot.business.system.dao;

import java.util.List;

import cn.emay.boot.business.system.pojo.Resource;
import cn.emay.orm.BaseSuperDao;

/**
 * 
 * @author Frank
 *
 */
public interface ResourceDao extends BaseSuperDao<Resource> {

	/**
	 * 获取用户所有的资源
	 */
	List<Resource> getUserResources(Long userId);

	/**
	 * 获取角色所有的资源
	 */
	List<Resource> getRoleResources(Long roleId);

}