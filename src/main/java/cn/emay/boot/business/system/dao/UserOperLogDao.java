package cn.emay.boot.business.system.dao;

import java.util.Date;

import cn.emay.boot.business.system.pojo.UserOperLog;
import cn.emay.orm.BaseSuperDao;
import cn.emay.utils.db.common.Page;

public interface UserOperLogDao extends BaseSuperDao<UserOperLog> {

	Page<UserOperLog> findByPage(String username, String content, Date startDate, Date endDate, int start, int limit);

}