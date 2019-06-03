package cn.emay.boot.service.system.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.emay.boot.dao.system.UserDao;
import cn.emay.boot.dao.system.UserRoleAssignDao;
import cn.emay.boot.dto.system.UserDTO;
import cn.emay.boot.pojo.system.User;
import cn.emay.boot.pojo.system.UserRoleAssign;
import cn.emay.boot.service.system.UserService;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.encryption.Md5;
import cn.emay.utils.result.Result;

/**
 * 
 * @author Frank
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private UserRoleAssignDao userRoleAssignDao;

	@Override
	public Result add(String username, String password, String realname, String mobile, String email, String remark, Long operatorId, Long[] roleIds) {
		if (username == null) {
			return Result.badResult("用户名为空");
		}
		if (password == null) {
			return Result.badResult("密码为空");
		}
		if(operatorId == null) {
			return Result.badResult("创建人为空");
		}
		if (roleIds == null || roleIds.length == 0) {
			return Result.badResult("角色为空");
		}
		User operator = userDao.findById(operatorId);
		if(operator == null) {
			return Result.badResult("创建人不存在");
		}
		User old = userDao.findByUserName(username);
		if(old != null) {
			return Result.badResult("用户名已存在");
		}
		String newPass = Md5.md5(password);
		User user = new User(username, newPass, realname, mobile, email, remark, operatorId);
		userDao.save(user);
		List<UserRoleAssign> urs = new ArrayList<>();
		for(Long roleId : roleIds) {
			urs.add(new UserRoleAssign(user.getId(), roleId));
		}
		userRoleAssignDao.saveBatch(urs);
		return Result.rightResult();
	}

	@Override
	public Result modify(Long userId, String realname, String mobile, String email, String remark, Long[] roleIds) {
		if (userId == null) {
			return Result.badResult("用户不存在");
		}
		User user = userDao.findById(userId);
		if(user == null) {
			return Result.badResult("用户不存在");
		}
		if (roleIds == null || roleIds.length == 0) {
			return Result.badResult("角色为空");
		}
		if(realname == null) {
			return Result.badResult("姓名不能为空");
		}
		user.setRealname(realname);
		user.setMobile(mobile);
		user.setEmail(email);
		user.setRemark(remark);
		userDao.update(user);
		List<UserRoleAssign> urs = new ArrayList<>();
		for(Long roleId : roleIds) {
			urs.add(new UserRoleAssign(user.getId(), roleId));
		}
		userRoleAssignDao.deleteByUserId(user.getId());
		userRoleAssignDao.saveBatch(urs);
		return Result.rightResult();
	}

	@Override
	public Result delete(Long userId) {
		userDao.deleteById(userId);
		userRoleAssignDao.deleteByUserId(userId);
		return Result.rightResult();
	}

	@Override
	public User findById(Long userId) {
		return userDao.findById(userId);
	}

	@Override
	public User findByUserName(String username) {
		return userDao.findByUserName(username);
	}

	@Override
	public Page<UserDTO> findPage(int start, int limit, String userName, int state) {
		Page<User> page = userDao.findPage(start, limit, userName, state);
		List<UserDTO> dtos = new ArrayList<UserDTO>();
		for (User user : page.getList()) {
			UserDTO dto = new UserDTO(user);
			dtos.add(dto);
		}
		Page<UserDTO> pagenew = new Page<>();
		pagenew.setNumByStartAndLimit(start, limit, page.getTotalCount());
		pagenew.setList(dtos);
		return pagenew;
	}

	@Override
	public Result on(Long userId) {
		userDao.updateState(userId, User.STATE_ON);
		return Result.rightResult();
	}

	@Override
	public Result off(Long userId) {
		userDao.updateState(userId, User.STATE_OFF);
		return Result.rightResult();
	}

	@Override
	public Result modifyPassword(Long userId, String newPassword) {
		User user = userDao.findById(userId);
		if(user == null) {
			return Result.badResult("用户不存在");
		}
		user.setPassword(Md5.md5(newPassword));
		userDao.update(user);
		return Result.rightResult();
	}

}
