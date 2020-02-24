package cn.emay.business.system.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.emay.base.dao.BasePojoSuperDaoImpl;
import cn.emay.business.system.dao.UserOperLogDao;
import cn.emay.business.system.pojo.UserOperLog;
import cn.emay.utils.db.common.Page;

/**
 * @author Frank
 */
@Repository
public class UserOperLogDaoImpl extends BasePojoSuperDaoImpl<UserOperLog> implements UserOperLogDao {

    @Override
    public Page<UserOperLog> findByPage(String username, String realname, String content, Date startDate, Date endDate, int start, int limit) {
        Map<String, Object> param = new HashMap<String, Object>();
        String hql = "from UserOperLog m where 1=1";
        if (!StringUtils.isEmpty(content)) {
            hql += " and  m.content like :context ";
            param.put("context", "%" + content + "%");
        }
        if (!StringUtils.isEmpty(username)) {
            hql += " and  m.username like :username ";
            param.put("username", "%" + username + "%");
        }
        if (!StringUtils.isEmpty(realname)) {
            hql += " and  m.realname like :realname ";
            param.put("realname", "%" + realname + "%");
        }
        if (startDate != null) {
            hql += " and m.operTime >= :startDate";
            param.put("startDate", startDate);
        }
        if (endDate != null) {
            hql += " and m.operTime <= :endDate";
            param.put("endDate", endDate);
        }
        hql += " order by m.id desc ";
        return this.getPageResult(hql, start, limit, param, UserOperLog.class);
    }

}