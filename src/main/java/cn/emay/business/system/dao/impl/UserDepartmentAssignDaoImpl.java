package cn.emay.business.system.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.emay.base.dao.BasePojoSuperDaoImpl;
import cn.emay.business.system.dao.UserDepartmentAssignDao;
import cn.emay.business.system.pojo.UserDepartmentAssign;

/**
 * 
 * @author Frank
 *
 */
@Repository
public class UserDepartmentAssignDaoImpl extends BasePojoSuperDaoImpl<UserDepartmentAssign> implements UserDepartmentAssignDao {

	@Override
	public void deleteByUserId(Long userId) {
		this.deleteByProperty("systemUserId", userId);
	}

	@Override
	public Long findByDepId(Long departmentId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select count(*) from UserDepartmentAssign where systemDepartmentId=:id";
		params.put("id", departmentId);
		return (Long) super.getUniqueResult(hql, params);
	}

}
