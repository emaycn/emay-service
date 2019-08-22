package cn.emay.boot.business.system.service;

import cn.emay.boot.business.system.dto.UserDTO;
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
	 * 保存用户及角色
	 */
	Result add(String username, String realname, String password, String email, String mobile, String roles, Long departmentId, User currentUser);

	/**
	 * 更新用户及角色
	 */
	Result modify(String username, String realname, String email, String mobile, String roles, Long userId, Long departmentId);

	/**
	 * 修改用户密码
	 */
	public Result modifyPassword(Long userId, String newPassword);

	/**
	 * 删除用户
	 */
	public Result delete(Long userId);

	/**
	 * 启用用户
	 */
	public Result on(Long userId);

	/**
	 * 停用用户
	 */
	public Result off(Long userId);

	/**
	 * 按照ID查找用户
	 */
	public User findById(Long userId);

	/**
	 * 重置用户密码
	 */
	Result updateResetUserPassword(User user);

	/**
	 * 按照用户名查找用户
	 */
	public User findByUserName(String username);

	/**
	 * 分页查询用户
	 */
	public Page<UserDTO> findPage(int start, int limit, String username, String realname, String mobile);

	/**
	 * 分页查询用户
	 */
	Page<UserDTO> findBycondition(String variableName, Long departmentId, int start, int limit);

	/**
	 * 用户名是否重复
	 */
	Long countByUserName(Long userId, String username);
}
