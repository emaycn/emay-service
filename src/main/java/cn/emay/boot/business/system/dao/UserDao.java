package cn.emay.boot.business.system.dao;

import cn.emay.boot.business.system.dto.UserDTO;
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
	User findByUserName(String username);

	/**
	 * 分页查询
	 */
	Page<UserDTO> findPage(int start, int limit, String username,String realname,String mobile);

	/**
	 * 更新状态
	 */
	void updateState(Long userId, int state);
	
	Page<User> findBycondition(String variableName, Long departmentId, int start, int limit);
	
	Long countByUserName(Long userId, String username);
}
