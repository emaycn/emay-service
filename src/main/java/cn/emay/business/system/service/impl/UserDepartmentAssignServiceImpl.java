package cn.emay.business.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private UserDepartmentAssignDao userDepartmentAssignDao;

	@Override
	public Long findByDepId(Long departmentId) {
		return userDepartmentAssignDao.findByDepId(departmentId);
	}

}
