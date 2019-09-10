package cn.emay.boot.business.system.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.boot.business.system.dao.UserOperLogDao;
import cn.emay.boot.business.system.pojo.User;
import cn.emay.boot.business.system.pojo.UserOperLog;
import cn.emay.boot.business.system.service.UserOperLogService;
import cn.emay.boot.utils.WebUtils;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.string.StringUtils;

/**
 * 
 * @author lijunjian
 *
 */
@Service
public class UserOperLogServiceImpl implements UserOperLogService {

	@Resource
	private UserOperLogDao userOperLogDao;

	@Override
	public void saveLogByCurrentUser(String module, String content, String type) {
		User user = WebUtils.getCurrentUser();
		if (user == null) {
			throw new IllegalArgumentException("no user login");
		}
		saveLog(module, user, content, type);
	}

	@Override
	public void saveLog(String module, User user, String content, String type) {
		UserOperLog log = new UserOperLog();
		log.setContent(content);
		log.setModule(module);
		log.setUsername(user.getUsername());
		log.setOperTime(new Date());
		log.setOperType(type);
		log.setUserId(user.getId());
		log.setRealname(user.getRealname());
		userOperLogDao.save(log);
	}

	@Override
	public Page<UserOperLog> findByPage(String username, String realname, String content, Date startDate, Date endDate, int start, int limit) {
		username = !StringUtils.isEmpty(username) ? username.toLowerCase() : username;
		return userOperLogDao.findByPage(username, realname, content, startDate, endDate, start, limit);
	}

	@Override
	public void savelogBatch(List<UserOperLog> logs) {
		userOperLogDao.savelogBatch(logs);
	}

	@Override
	public List<UserOperLog> findList(String username, String realname, String content, Date startDate, Date endDate, int start, int limit) {
		username = !StringUtils.isEmpty(username) ? username.toLowerCase() : username;
		return userOperLogDao.findList(username, realname, content, startDate, endDate, start, limit);
	}

}