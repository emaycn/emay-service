package cn.emay.business.system.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.emay.base.dao.BasePojoSuperDaoImpl;
import cn.emay.business.system.dao.UserDao;
import cn.emay.business.system.pojo.User;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.regular.RegularUtils;

/**
 * @author frank
 */
@Repository
public class UserDaoImpl extends BasePojoSuperDaoImpl<User> implements UserDao {

    @Override
    public Page<User> findPage(int start, int limit, String username, String realname, String mobile, Integer userState, String userFor) {
        String hql = "from User where userFor=:userFor ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userFor", userFor);
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

    @Override
    public List<User> findByIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return null;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = "from User where id in (:userIds)";
        params.put("userIds", userIds);
        return this.getListResult(User.class, hql, params);
    }

    @Override
    public List<Long> findIdsByRealName(String realName) {
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = "select id from User where realname like :realName";
        params.put("realName", "%" + RegularUtils.specialCodeEscape(realName) + "%");
        return this.getListResult(Long.class, hql, params);
    }

    @Override
    public List<Long> findIdsByRealNameAndUserType(String realName, Integer userType) {
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = "select id from User where realname like :realName";
        if (userType != null && userType.intValue() > -1) {
            hql += " and userType = :userType";
            params.put("userType", userType);
        }
        params.put("realName", "%" + RegularUtils.specialCodeEscape(realName) + "%");
        return this.getListResult(Long.class, hql, params);
    }

    @Override
    public Page<User> findClientPage(int start, int limit, String username, String realname, String mobile, Integer userState, Long clintId) {
        String sql = "select u.* from system_user u,client_user_assign cu where u.id=cu.user_id and cu.client_id=?  ";
        List<Object> params = new ArrayList<>();
        params.add(clintId);
        if (!StringUtils.isEmpty(username)) {
            sql += " and u.username like ? ";
            params.add("%" + username + "%");
        }
        if (!StringUtils.isEmpty(realname)) {
            sql += " and u.realname like ? ";
            params.add("%" + realname + "%");
        }
        if (!StringUtils.isEmpty(mobile)) {
            sql += " and mobile = ? ";
            params.add(mobile);
        }
        if (null != userState && -1 != userState) {
            sql += " and user_state =?";
            params.add(userState);
        }
        sql += " order by u.id desc ";
        return this.findObjectPageByClassInMysql(User.class, sql, start, limit, params.toArray());
    }

    @Override
    public List<User> findUsersByClientId(Long clientId) {
        String sql = "select u.* from system_user u,client_user_assign cu where u.id=cu.user_id and cu.client_id=?  ";
        List<Object> params = new ArrayList<>();
        params.add(clientId);
        sql += " order by u.id desc ";
        return this.findObjectListByClass(User.class, sql, params);
    }

}