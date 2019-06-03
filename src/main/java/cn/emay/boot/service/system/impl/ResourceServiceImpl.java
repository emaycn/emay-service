package cn.emay.boot.service.system.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.emay.boot.dao.system.ResourceDao;
import cn.emay.boot.pojo.system.Resource;
import cn.emay.boot.service.system.ResourceService;

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
	public List<Resource> getUserResources(Long userId) {
		return resourceDao.getUserResources(userId);
	}

	@Override
	public List<Resource> getRoleResources(Long roleId) {
		return resourceDao.getRoleResources(roleId);
	}


}
