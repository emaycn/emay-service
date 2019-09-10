package cn.emay.boot.business.system.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.emay.boot.base.dao.BasePojoSuperDaoImpl;
import cn.emay.boot.business.system.dao.UserDao;
import cn.emay.boot.business.system.pojo.User;
import cn.emay.utils.db.common.Page;

/**
 * @author frank
 */
@Repository
public class UserDaoImpl extends BasePojoSuperDaoImpl<User> implements UserDao {

	@Override
	public Page<User> findPage(int start, int limit, String username, String realname, String mobile, Integer userState) {
		String hql = "from User where 1=1 ";
		Map<String, Object> params = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(username)) {
			hql += " and username like :username ";
			params.put("username", "%" + username + "%");
		}
		if (!StringUtils.isEmpty(realname)) {
			hql += " and realname like :realname ";
			params.put("realname", "%" + realname + "%");
		}
		if (!StringUtils.isEmpty(mobile)) {
			hql += " and mobile like :mobile ";
			params.put("mobile", "%" + mobile + "%");
		}
		boolean isHasUserState = userState != null && (userState == 1 || userState == 0);
		if (isHasUserState) {
			hql += " and userState = :userState ";
			params.put("userState", userState);
		}
		hql += " order by id desc ";
		return this.getPageResult(hql, start, limit, params, User.class);
	}

	@Override
	public Page<User> findBycondition(String variableName, Long departmentId, int start, int limit) {
		Map<String, Object> param = new HashMap<String, Object>(8);
		String hql = "select u from User u,UserDepartmentAssign ud where u.id=ud.systemUserId and ud.systemDepartmentId=:departmentId";
		param.put("departmentId", departmentId);
		if (!StringUtils.isEmpty(variableName)) {
			hql += " and (u.username like :variableName or u.realname like :variableName or u.mobile like :variableName)";
			param.put("variableName", "%" + variableName + "%");
		}
		hql += " order by u.id desc ";
		Page<User> page = this.getPageResult(hql, start, limit, param, User.class);
		return page;
	}

	@Override
	public Boolean hasSameUserName(String username) {
		String hql = "select count(*) from User where username =:username";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		return (Long) super.getUniqueResult(hql, params) > 0;
	}

	@Override
	public void updateState(Long userId, int state) {
		String hql = " update User user set user.userState = :state where user.id = :id ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("state", state);
		params.put("id", userId);
		this.execByHql(hql, params);
	}

	@Override
	public User findByUserName(String username) {
		return this.findByProperty("username", username);
	}

}