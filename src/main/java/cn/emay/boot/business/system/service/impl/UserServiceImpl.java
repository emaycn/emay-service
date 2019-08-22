package cn.emay.boot.business.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.emay.boot.business.system.dao.DepartmentDao;
import cn.emay.boot.business.system.dao.RoleDao;
import cn.emay.boot.business.system.dao.UserDao;
import cn.emay.boot.business.system.dao.UserDepartmentAssignDao;
import cn.emay.boot.business.system.dao.UserRoleAssignDao;
import cn.emay.boot.business.system.dto.UserDTO;
import cn.emay.boot.business.system.pojo.Department;
import cn.emay.boot.business.system.pojo.Role;
import cn.emay.boot.business.system.pojo.User;
import cn.emay.boot.business.system.pojo.UserDepartmentAssign;
import cn.emay.boot.business.system.pojo.UserRoleAssign;
import cn.emay.boot.business.system.service.UserService;
import cn.emay.boot.utils.PasswordUtils;
import cn.emay.boot.utils.RandomNumberUtils;
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
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private UserDepartmentAssignDao userDepartmentAssignDao;
	@Autowired
	private RoleDao roleDao;

	@Override
	public Result add(String username, String realname, String password, String email, String mobile, String roles, Long departmentId, User currentUser) {
		List<UserRoleAssign> urs = new ArrayList<UserRoleAssign>();
		String message = this.getUserRoles(roles, urs);
		if (!StringUtils.isEmpty(message)) {
			return Result.badResult(message);
		}
		if (urs.size() == 0) {
			return Result.badResult("角色权限不能为空");
		}
		if (this.countByUserName(0L, username) > 0) {
			return Result.badResult("用户名已存在");
		}
		Department department = departmentDao.findById(departmentId);
		if (department == null) {
			return Result.badResult("部门不存在");
		}
		if (urs.size() == 0) {
			return Result.badResult("权限不能为空");
		}
		User user = new User(username, password, realname, mobile, email, "", currentUser.getId());
		userDao.save(user);
		for (UserRoleAssign ur : urs) {
			ur.setUserId(user.getId());
			ur.setCreatorId(currentUser.getId());
		}
		userRoleAssignDao.saveBatch(urs);
		UserDepartmentAssign userDepartmentAssign = new UserDepartmentAssign();
		userDepartmentAssign.setSystemUserId(user.getId());
		userDepartmentAssign.setSystemDepartmentId(departmentId);
		userDepartmentAssignDao.save(userDepartmentAssign);
		return Result.rightResult();
	}

	@Override
	public Result modify(String username, String realname, String email, String mobile, String roleIds, Long userId, Long departmentId) {
		User user = userDao.findById(userId);
		if (user == null) {
			return Result.badResult("用户不存在");
		}
		List<UserRoleAssign> urs = new ArrayList<UserRoleAssign>();
		String message = this.getUserRoles(roleIds, urs);
		if (!StringUtils.isEmpty(message)) {
			return Result.badResult(message);
		}
		if (urs.size() == 0) {
			return Result.badResult("权限不能为空");
		}
		List<Long> ids = new ArrayList<Long>();
		for (UserRoleAssign ur : urs) {
			ur.setUserId(user.getId());
			ids.add(ur.getRoleId());
		}
		user.setUsername(username);
		user.setRealname(realname);
		user.setEmail(email);
		user.setMobile(mobile);
		userDao.update(user);
		UserDepartmentAssign userDepartmentAssign = new UserDepartmentAssign();
		userDepartmentAssign.setSystemDepartmentId(departmentId);
		userDepartmentAssign.setSystemUserId(userId);
		if (user.getId().longValue() != 1) {
			userRoleAssignDao.deleteByUserId(user.getId());
			userRoleAssignDao.saveBatch(urs);
			userDepartmentAssignDao.deleteDataByUserId(userId);
			userDepartmentAssignDao.save(userDepartmentAssign);
		}
		return Result.rightResult();
	}

	private String getUserRoles(String roleIds, List<UserRoleAssign> urs) {
		String message = "";
		String[] roleIdArray = roleIds.split(",");
		Set<Long> roleIdSet = new HashSet<Long>();
		for (String roleId : roleIdArray) {
			roleIdSet.add(Long.valueOf(roleId));
		}
		List<Role> roles = roleDao.findAllRole();
		Map<Long, Role> map = new HashMap<Long, Role>(roles.size());
		for (Role role : roles) {
			map.put(role.getId(), role);
		}
		for (Long set : roleIdSet) {
			if (!map.containsKey(set)) {
				message = "数据错误";
			} else {
				UserRoleAssign ur = new UserRoleAssign();
				ur.setRoleId(set);
				urs.add(ur);
			}
		}
		return message;
	}

	@Override
	public Result delete(Long userId) {
		userDao.updateState(userId, User.STATE_DELETE);
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
	public Page<UserDTO> findPage(int start, int limit, String username, String realname, String mobile) {
		Page<UserDTO> page = userDao.findPage(start, limit, username, realname, mobile);
		return page;
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
		if (user == null) {
			return Result.badResult("用户不存在");
		}
		user.setPassword(Md5.md5(newPassword));
		userDao.update(user);
		return Result.rightResult();
	}

	@Override
	public Page<UserDTO> findBycondition(String variableName, Long departmentId, int start, int limit) {
		Page<User> page = userDao.findBycondition(variableName, departmentId, start, limit);
		Page<UserDTO> pagedto = new Page<UserDTO>();
		List<UserDTO> listdto = new ArrayList<UserDTO>();
		Department department = departmentDao.findById(departmentId);
		Department parentDepartment = departmentDao.findById(department.getParentDepartmentId());
		for (User user : page.getList()) {
			UserDTO dto = new UserDTO(user, department, parentDepartment);
			listdto.add(dto);
		}
		pagedto.setList(listdto);
		pagedto.setCurrentPageNum(page.getCurrentPageNum());
		pagedto.setLimit(page.getLimit());
		pagedto.setStart(page.getStart());
		pagedto.setTotalCount(page.getTotalCount());
		pagedto.setTotalPage(page.getTotalPage());
		return pagedto;
	}

	@Override
	public Long countByUserName(Long userId, String username) {
		return userDao.countByUserName(userId, username);
	}

	@Override
	public Result updateResetUserPassword(User user) {
		String randomPwd = RandomNumberUtils.getNumbersAndLettersRandom(6);
		String newEnPassword = PasswordUtils.encrypt(Md5.md5(randomPwd.getBytes()));
		user.setPassword(newEnPassword);
		user.setLastChangePasswordTime(null);
		userDao.update(user);
		return Result.rightResult(randomPwd);
	}

}
