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
	 * 分页查询用户
	 * 
	 * @param start
	 *            其实数据位置
	 * @param limit
	 *            查询的数据条数
	 * @param username
	 *            用户名
	 * @param realname
	 *            用户姓名
	 * @param mobile
	 *            手机号
	 * @param userState
	 *            用户状态
	 * @return
	 */
	Page<User> findPage(int start, int limit, String username, String realname, String mobile, Integer userState);

	/**
	 * 用户名是否重复
	 * 
	 * @param ignoreUserId
	 *            排除的用户ID
	 * @param username
	 *            用户名
	 * @return
	 */
	Boolean hasSameUserName(String username);

	/**
	 * 更新状态
	 * 
	 * @param userId
	 *            用户ID
	 * @param state
	 *            用户状态
	 */
	void updateState(Long userId, int state);

	/*---------------------------------*/

	/**
	 * 根据用户名查询用户
	 */
	User findByUserName(String username);

	Page<User> findBycondition(String variableName, Long departmentId, int start, int limit);

}
