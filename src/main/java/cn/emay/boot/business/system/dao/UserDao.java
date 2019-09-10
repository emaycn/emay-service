package cn.emay.boot.business.system.dao;

import java.util.List;

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

	/**
	 * 按照用户名查找用户
	 * 
	 * @param username
	 *            用户名
	 * @return
	 */
	User findByUserName(String username);

	/**
	 * 按照部门及同一条件分页查询
	 * 
	 * @param variableName
	 *            手机/用户名/姓名 综合查询条件
	 * @param departmentId
	 *            部门ID
	 * @param start
	 *            起始
	 * @param limit
	 *            条数
	 * @return
	 */
	Page<User> findBycondition(String variableName, Long departmentId, int start, int limit);

	/**
	 * @param start
	 * @param limit
	 * @return
	 */
	List<User> findAllByPage(int start, int limit);

}
