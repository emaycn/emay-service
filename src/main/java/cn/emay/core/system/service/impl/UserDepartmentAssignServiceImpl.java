package cn.emay.core.system.service.impl;

import cn.emay.core.system.dao.UserDepartmentAssignDao;
import cn.emay.core.system.service.UserDepartmentAssignService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Frank
 */
@Service
public class UserDepartmentAssignServiceImpl implements UserDepartmentAssignService {

    @Resource
    private UserDepartmentAssignDao userDepartmentAssignDao;

    @Override
    public Long findByDepId(Long departmentId) {
        return userDepartmentAssignDao.findByDepId(departmentId);
    }

}
