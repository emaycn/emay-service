package cn.emay.boot.business.system.dao;

import cn.emay.boot.business.system.pojo.User;
import cn.emay.orm.BaseSuperDao;
import cn.emay.utils.db.common.Page;

/**
 * 
 * @author Frank
 *
 */
public interface UserDao extends BaseSuperDao<User> {
	
	/**
	 * 根据用户名查询用户
	 */
	public User findByUserName(String username);

	/**
	 * 分页查询
	 */
	public Page<User> findPage(int start, int limit, String userName, int state);

	/**
	 * 更新状态
	 */
	public void updateState(Long userId, int state);

}
