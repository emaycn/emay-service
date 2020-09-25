package cn.emay.core.system.service.impl;

import cn.emay.core.system.dao.ResourceDao;
import cn.emay.core.system.pojo.Resource;
import cn.emay.core.system.service.ResourceService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Frank
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    @javax.annotation.Resource
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
    public List<Resource> getByResourceType(String resourceType) {
        return resourceDao.getByResourceType(resourceType);
    }

}
