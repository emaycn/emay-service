package cn.emay.boot.business.system.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.emay.boot.base.dao.BaseSuperDaoImpl;
import cn.emay.boot.business.system.dao.UserDepartmentAssignDao;
import cn.emay.boot.business.system.pojo.UserDepartmentAssign;

@Repository
public class UserDepartmentAssignDaoImpl extends BaseSuperDaoImpl<UserDepartmentAssign> implements UserDepartmentAssignDao{

	@Override
	public Long findByDepId(Long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select count(*) from UserDepartmentAssign where enterpriseDepartmentId=:id";
		params.put("id", id);
		return (Long) super.getUniqueResult(hql, params);
	}

	@Override
	public void deleteDataByUserId(Long userId) {
		this.deleteByProperty("systemUserId", userId);
	}

	@Override
	public UserDepartmentAssign findByUserId(Long userId) {
		return this.findByProperty("systemUserId", userId);
	}
	
}
