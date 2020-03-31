package cn.emay.business.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.emay.business.system.dao.UserRoleAssignDao;
import cn.emay.business.system.pojo.UserRoleAssign;
import cn.emay.business.system.service.UserRoleAssignService;

/**
 * 
 * @author frank
 *
 */
@Service
public class UserRoleAssignServiceImpl implements UserRoleAssignService {

	@Autowired
	private UserRoleAssignDao userRoleAssignDao;

	@Override
	public Boolean findExistsByRoleId(Long roleId) {
		Boolean isExists = true;
		List<UserRoleAssign> list = userRoleAssignDao.findByRoleId(roleId);
		if (null == list || list.isEmpty()) {
			isExists = false;
		}
		return isExists;
	}

	@Override
	public List<UserRoleAssign> findByUserId(Long userId) {
		return userRoleAssignDao.findByUserId(userId);
	}

}
