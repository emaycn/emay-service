package cn.emay.business.system.service;

import java.util.Date;

import cn.emay.business.system.pojo.UserOperLog;
import cn.emay.constant.web.OperType;
import cn.emay.utils.db.common.Page;

/**
 * 
 * @author lijunjian
 *
 */
public interface UserOperLogService {
	
	/**
	 * 存储运营系统日志<br/>
	 * 自动获取当前用户,防止传入其他用户<br/>
	 * 在做微服务化时，要注意非API分层不可取,可将此API拆分成两层
	 * 
	 * @param module
	 *            模块
	 * @param content
	 *            日志内容
	 * @param type
	 *            操作类型
	 */
	void saveOperLog(String module, String content, OperType type);
	
	/**
	 * 分页查询
	 * 
	 * @param username
	 *            用户名
	 * @param realname
	 *            姓名
	 * @param content
	 *            日志内容
	 * @param startDate
	 *            日志记录起始时间
	 * @param endDate
	 *            日志记录结束时间
	 * @param start
	 *            从第几条开始查
	 * @param limit
	 *            查几条
	 * @return
	 */
	Page<UserOperLog> findByPage(String username, String realname, String content, Date startDate, Date endDate,int start, int limit);
	
}