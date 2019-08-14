package cn.emay.boot.business.system.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.emay.boot.base.dao.BaseSuperDaoImpl;
import cn.emay.boot.business.system.dao.UserDao;
import cn.emay.boot.business.system.dto.UserDTO;
import cn.emay.boot.business.system.pojo.User;
import cn.emay.boot.utils.CheckUtil;
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
	public Page<UserDTO> findPage(int start, int limit, String username,String realname,String mobile) {
		List<Object> params = new ArrayList<Object>();
		String sql = "SELECT u.*, d.department_name as department FROM system_user u LEFT JOIN system_user_department_assign ude ON u.id = ude.system_user_id "
				+ " LEFT JOIN system_enterprise_department d ON d.id = ude.enterprise_department_id WHERE u.user_state != ? ";
		params.add(User.STATE_DELETE);
		if (username != null && username.trim().length() > 0) {
			sql += " and u.username like ? ";
			params.add("%"+CheckUtil.specialCodeEscape(username)+"%");
		}
		if (realname != null && realname.trim().length() > 0) {
			sql += " and u.realname like ? ";
			params.add("%"+CheckUtil.specialCodeEscape(realname)+"%");
		}
		if (mobile != null && mobile.trim().length() > 0) {
			sql += " and u.mobile like ? ";
			params.add("%"+CheckUtil.specialCodeEscape(mobile)+"%");
		}
		sql += " order by u.id desc ";
		Page<UserDTO> page = this.findSqlForPageForMysql(UserDTO.class, sql, params, start, limit);
		List<Long> ids = new ArrayList<Long>();
		for (UserDTO u : page.getList()) {
			ids.add(u.getId());
		}
		if (ids.size() > 0) {
			String sql1 = "select r.role_name,ur.user_id from system_role r,system_user_role_assign ur where r.id = ur.role_id and "
					+ " ur.user_id in ("+org.apache.commons.lang3.StringUtils.join(ids.toArray(), ",")+")";
			List<Map<String, Object>> list1 = this.getJdbcTemplate().queryForList(sql1);
			Map<Long, String> rolenames = new HashMap<Long, String>();
			for (Map<String, Object> map : list1) {
				String roleName = map.get("role_name").toString();
				Long userId = (Long) map.get("user_id");
				if (rolenames.containsKey(userId)) {
					rolenames.put(userId.longValue(), rolenames.get(userId) + "," + roleName);
				} else {
					rolenames.put(userId.longValue(), roleName);
				}
			}
			for (UserDTO o : page.getList()) {
				o.setRolename(rolenames.get(o.getId()));
			}
		}
		return page;
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
	public Page<User> findBycondition(String variableName, Long departmentId, int start, int limit) {
		Map<String, Object> param = new HashMap<String, Object>();
		String hql = "select u from User u,UserDepartmentAssign ud where u.id=ud.systemUserId and u.userState != :state and ud.enterpriseDepartmentId=:departmentId";
		param.put("departmentId", departmentId);
		if (!StringUtils.isEmpty(variableName)) {
			hql += " and (u.username like :variableName or u.realname like :variableName or u.mobile like :variableName)";
			param.put("variableName", "%" + CheckUtil.specialCodeEscape(variableName) + "%");
		}
		param.put("state", User.STATE_DELETE);
		hql += " order by u.createTime desc ";
		Page<User> page = this.getPageResult(hql, start, limit, param, User.class);
		return page;
	}

	@Override
	public Long countByUserName(Long userId, String username) {
		String hql = "select count(*) from User where userState != :deletestate and username =:username";
		Map<String, Object> params = new HashMap<String, Object>();
		if (userId != null && userId.longValue() > 0L) {
			hql = hql + " and id !=:id";
			params.put("id", userId);
		}
		params.put("deletestate", User.STATE_DELETE);
		params.put("username", username);
		return (Long) super.getUniqueResult(hql, params);
	}

}