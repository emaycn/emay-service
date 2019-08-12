package cn.emay.boot.business.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.boot.business.system.dao.UserRoleAssignDao;
import cn.emay.boot.business.system.pojo.UserRoleAssign;
import cn.emay.boot.business.system.service.UserRoleAssignService;

@Service
public class UserRoleAssignServiceImpl implements UserRoleAssignService{

	@Resource
	private UserRoleAssignDao userRoleAssignDao;

	@Override
	public List<UserRoleAssign> findByUserId(Long userId) {
		return userRoleAssignDao.findByUserId(userId);
	}
	
	
	
	
	
	
}
