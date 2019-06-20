package cn.emay.boot.business.system.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.emay.boot.base.dao.BaseSuperDaoImpl;
import cn.emay.boot.business.system.dao.UserDao;
import cn.emay.boot.business.system.pojo.User;
import cn.emay.utils.db.common.Page;

/**
 * @author frank
 */
@Repository
public class UserDaoImpl extends BaseSuperDaoImpl<User> implements UserDao {

	@Override
	public User findByUserName(String username) {
		return this.findByProperty("username", username);
	}

	@Override
	public Page<User> findPage(int start, int limit, String userName, int state) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from User where 1 = 1 ";
		if (!StringUtils.isEmpty(userName)) {
			hql += " and  username like :userName ";
			params.put("userName", "%" + userName + "%");
		}
		if (state != -1) {
			hql += " and state = :state ";
			params.put("state", state);
		}
		hql += " order by createTime desc ";
		return this.getPageResult(hql, start, limit, params, User.class);
	}

	@Override
	public void updateState(Long userId, int state) {
		String hql = " update User user set user.state = :state where user.id = :id ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("state", state);
		params.put("id", userId);
		this.execByHql(hql, params);
	}

}