package cn.emay.boot.service.system.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.emay.boot.dao.system.RoleDao;
import cn.emay.boot.dao.system.RoleResourceAssignDao;
import cn.emay.boot.pojo.system.Role;
import cn.emay.boot.pojo.system.RoleResourceAssign;
import cn.emay.boot.service.system.RoleService;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.Result;

/**
 * 
 * @author Frank
 *
 */
@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;
	@Autowired
	private RoleResourceAssignDao roleResourceAssignDao;

	@Override
	public Result add(String roleName, String remark, Long[] resourceIds) {
		if (roleName == null) {
			return Result.badResult("角色名为空");
		}
		if (resourceIds == null || resourceIds.length == 0) {
			return Result.badResult("资源为空");
		}
		Role old = roleDao.findByRoleName(roleName);
		if (old != null) {
			return Result.badResult("角色名已存在");
		}
		Role role = new Role(roleName, remark);
		roleDao.save(role);
		List<RoleResourceAssign> urs = new ArrayList<>();
		for(Long id : resourceIds) {
			urs.add(new RoleResourceAssign(role.getId(), id));
		}
		roleResourceAssignDao.saveBatch(urs);
		return Result.rightResult();
	}

	@Override
	public Result modify(Long roleId, String roleName, String remark, Long[] resourceIds) {
		if (roleId == null) {
			return Result.badResult("角色不存在");
		}
		Role role = roleDao.findById(roleId);
		if(role == null) {
			return Result.badResult("角色不存在");
		}
		if (resourceIds == null || resourceIds.length == 0) {
			return Result.badResult("资源为空");
		}
		role.setRoleName(roleName);
		role.setRemark(remark);
		roleDao.update(role);
		List<RoleResourceAssign> urs = new ArrayList<>();
		for(Long id : resourceIds) {
			urs.add(new RoleResourceAssign(role.getId(), id));
		}
		roleResourceAssignDao.deleteByRoleId(roleId);
		roleResourceAssignDao.saveBatch(urs);
		return Result.rightResult();
	}

	@Override
	public Result delete(Long roleId) {
		roleDao.deleteById(roleId);
		return Result.rightResult();
	}

	@Override
	public Role findById(Long roleId) {
		return roleDao.findById(roleId);
	}

	@Override
	public Page<Role> findPage(int start, int limit) {
		return roleDao.findPage(start, limit);
	}

	@Override
	public List<Role> getUserRoles(Long userId) {
		return roleDao.getUserRoles(userId);
	}

	@Override
	public List<Role> findAll() {
		return roleDao.findAll();
	}

}
