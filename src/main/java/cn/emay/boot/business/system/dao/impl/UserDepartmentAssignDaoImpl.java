package cn.emay.boot.business.system.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.emay.boot.base.dao.BasePojoSuperDaoImpl;
import cn.emay.boot.business.system.dao.UserDepartmentAssignDao;
import cn.emay.boot.business.system.pojo.UserDepartmentAssign;

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

	/*---------------------------------*/

	@Override
	public Long findByDepId(Long id) {
		Map<String, Object> params = new HashMap<String, Object>(4);
		String hql = "select count(*) from UserDepartmentAssign where systemDepartmentId=:id";
		params.put("id", id);
		return (Long) super.getUniqueResult(hql, params);
	}

}
