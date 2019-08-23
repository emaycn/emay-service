package cn.emay.boot.business.system.service;

import cn.emay.boot.business.system.dto.UserDTO;
import cn.emay.boot.business.system.dto.UserItemDTO;
import cn.emay.boot.business.system.pojo.User;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.Result;

/**
 * 
 * @author Frank
 *
 */
public interface UserService {

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
	Page<UserItemDTO> findPage(int start, int limit, String username, String realname, String mobile, Integer userState);

	/**
	 * 保存用户及角色
	 */
	/**
	 * 新增用户<br/>
	 * 保存用户、角色关联、部门关联
	 * 
	 * @param username
	 *            用户名
	 * @param realname
	 *            姓名
	 * @param password
	 *            密码
	 * @param email
	 *            邮箱
	 * @param mobile
	 *            手机号
	 * @param roleIds
	 *            角色Ids
	 * @param departmentId
	 *            部门Id
	 * @param operator
	 *            创建人
	 * @return
	 */
	Result add(String username, String realname, String password, String email, String mobile, String roleIds, Long departmentId, User operator);

	/**
	 * 按照ID查找用户
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	User findById(Long userId);

	/**
	 * 停用用户
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	Result off(Long userId);

	/**
	 * 启用用户
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	Result on(Long userId);

	/**
	 * 删除用户
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	Result delete(Long userId);

	/**
	 * 重置用户密码
	 * 
	 * @param userId
	 *            用户ID
	 * @param newPassword
	 *            新密码
	 * @return
	 */
	Result resetPassword(Long userId, String newPassword);

	/**
	 * 更新用户以及相关的角色、部门关联
	 * 
	 * @param realname
	 * @param email
	 * @param mobile
	 * @param roles
	 * @param userId
	 * @param departmentId
	 * @return
	 */
	Result modify(String realname, String email, String mobile, String roles, Long userId, Long departmentId);

	/**
	 * 修改用户密码
	 * 
	 * @param userId
	 *            用户ID
	 * @param newPassword
	 *            新密码
	 * @return
	 */
	Result modifyPassword(Long userId, String newPassword);

	/**
	 * 按照用户名查找用户
	 * 
	 * @param username
	 *            用户名
	 * @return
	 */
	User findByUserName(String username);

	/*---------------------------------*/

	/**
	 * 分页查询用户
	 */
	Page<UserDTO> findBycondition(String variableName, Long departmentId, int start, int limit);

}
