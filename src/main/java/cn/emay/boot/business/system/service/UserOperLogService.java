package cn.emay.boot.business.system.service;

import java.util.Date;

import cn.emay.boot.business.system.pojo.UserOperLog;
import cn.emay.utils.db.common.Page;

/**
 * 
 * @author lijunjian
 *
 */
public interface UserOperLogService {

	/**
	 * 存储日志
	 * 
	 */
	void saveLog(String module, Long userId,String username, String content, String type);
	
	/**
	 * 分页查询
	 * 
	 */
	Page<UserOperLog> findByPage(String username, String content, Date startDate, Date endDate, int start, int limit);

}