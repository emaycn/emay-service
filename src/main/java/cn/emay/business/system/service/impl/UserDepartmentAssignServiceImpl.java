package cn.emay.business.system.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.business.system.dao.UserDepartmentAssignDao;
import cn.emay.business.system.service.UserDepartmentAssignService;

/**
 * 
 * @author Frank
 *
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
