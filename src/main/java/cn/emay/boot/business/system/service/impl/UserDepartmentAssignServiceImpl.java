package cn.emay.boot.business.system.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.boot.business.system.dao.UserDepartmentAssignDao;
import cn.emay.boot.business.system.service.UserDepartmentAssignService;

/**
 * 
 * @author Frank
 *
 */
@Service
public class UserDepartmentAssignServiceImpl implements UserDepartmentAssignService {

	@Resource
	private UserDepartmentAssignDao userDepartmentAssignDao;

	/*---------------------------------*/

	@Override
	public Long findByDepId(Long id) {
		return userDepartmentAssignDao.findByDepId(id);
	}

}
