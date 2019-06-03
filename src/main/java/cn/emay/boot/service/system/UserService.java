package cn.emay.boot.service.system;

import cn.emay.boot.dto.system.UserDTO;
import cn.emay.boot.pojo.system.User;
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
	public Result add(String username, String password, String realname, String mobile, String email, String remark, Long operatorId, Long[] roleIds);

	/**
	 * 更新用户及角色
	 */
	public Result modify(Long userId, String realname, String mobile, String email, String remark, Long[] roleIds);
	
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
	 * 按照用户名查找用户
	 */
	public User findByUserName(String username);

	/**
	 * 分页查询用户
	 */
	public Page<UserDTO> findPage(int start, int limit, String userName, int state);

}
