package cn.emay.boot.business.system.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.boot.business.system.dao.UserOperLogDao;
import cn.emay.boot.business.system.pojo.UserOperLog;
import cn.emay.boot.business.system.service.UserOperLogService;
import cn.emay.boot.utils.CheckUtil;
import cn.emay.utils.db.common.Page;


@Service
public class UserOperLogServiceImpl implements UserOperLogService {

	@Resource
	private UserOperLogDao userOperLogDao;

	@Override
	public void saveLog(String module, Long userId,String username, String content, String type) {
		UserOperLog log = new UserOperLog();
		log.setContent(content);
		log.setModule(module);
		log.setUsername(username);
		log.setOperTime(new Date());
		log.setOperType(type);
		log.setUserId(userId);
		userOperLogDao.save(log);
	}

	@Override
	public Page<UserOperLog> findByPage(String username, String content, Date startDate, Date endDate, int start, int limit) {
		if(!CheckUtil.isEmpty(username)){
			username = username.toLowerCase();
		}
		return userOperLogDao.findByPage(username, content, startDate, endDate, start, limit);
	}

}