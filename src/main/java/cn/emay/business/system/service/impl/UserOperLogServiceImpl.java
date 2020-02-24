package cn.emay.business.system.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.business.client.service.ClientService;
import cn.emay.business.system.dao.UserOperLogDao;
import cn.emay.business.system.pojo.User;
import cn.emay.business.system.pojo.UserOperLog;
import cn.emay.business.system.service.UserOperLogService;
import cn.emay.business.system.service.UserService;
import cn.emay.constant.web.OperType;
import cn.emay.utils.WebUtils;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.string.StringUtils;

/**
 * @author chang
 */
@Service
public class UserOperLogServiceImpl implements UserOperLogService {

	@Resource
	private UserOperLogDao userOperLogDao;
	@Resource
	private ClientService clientService;
	@Resource
	private UserService userService;

	@Override
	public Page<UserOperLog> findByPage(String username, String realname, String content, Date startDate, Date endDate, int start, int limit) {
		username = !StringUtils.isEmpty(username) ? username.toLowerCase() : username;
		return userOperLogDao.findByPage(username, realname, content, startDate, endDate, start, limit);
	}

	@Override
	public void saveOperLog(String module, String content, OperType type) {
		User user = WebUtils.getCurrentUser();
		if (user == null) {
			throw new IllegalArgumentException("no user login");
		}
		UserOperLog log = new UserOperLog();
		log.setContent(content);
		log.setModule(module);
		log.setUsername(user.getUsername());
		log.setOperTime(new Date());
		log.setOperType(type.getType());
		log.setUserId(user.getId());
		log.setRealname(user.getRealname());
		userOperLogDao.save(log);
	}

}