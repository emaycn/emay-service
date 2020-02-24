package cn.emay.business.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.emay.business.system.dao.ResourceDao;
import cn.emay.business.system.pojo.Resource;
import cn.emay.business.system.service.ResourceService;

/**
 * 
 * @author Frank
 *
 */
@Service
public class ResourceServiceImpl implements ResourceService {

	@Autowired
	private ResourceDao resourceDao;

	@Override
	public List<Resource> getAll() {
		return resourceDao.findAll();
	}

	@Override
	public List<Resource> getRoleResources(Long roleId) {
		return resourceDao.getRoleResources(roleId);
	}

	@Override
	public List<Resource> getUserResources(Long userId) {
		return resourceDao.getUserResources(userId);
	}
	
	@Override
	public List<Resource> getByResourceType(String resourceType){
		return resourceDao.getByResourceType(resourceType);
	}

}