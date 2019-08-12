package cn.emay.boot.business.system.service;

import java.util.Date;

import cn.emay.boot.business.system.pojo.UserOperLog;
import cn.emay.utils.db.common.Page;

/**
 * 
* @项目名称：ebdp-web-operation 
* @类描述：日志管理   
* @创建人：lijunjian   
* @创建时间：2019年7月5日 下午2:55:53   
* @修改人：lijunjian   
* @修改时间：2019年7月5日 下午2:55:53   
* @修改备注：
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