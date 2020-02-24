package cn.emay.business.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.emay.business.system.dao.RoleDao;
import cn.emay.business.system.dao.RoleResourceAssignDao;
import cn.emay.business.system.pojo.Role;
import cn.emay.business.system.pojo.RoleResourceAssign;
import cn.emay.business.system.service.RoleService;
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
	public Page<Role> findPage(int start, int limit, String roleName, String roleType) {
		return roleDao.findPage(start, limit, roleName, roleType);
	}

	@Override
	public Role findById(Long roleId) {
		return roleDao.findById(roleId);
	}

	@Override
	public boolean hasSameRoleName(String roleName, Long ignoreRoleId) {
		return roleDao.hasSameRoleName(roleName, ignoreRoleId);
	}

	@Override
	public Result add(String roleName, String remark, List<RoleResourceAssign> roleResourceAssignList, String roleType) {
		Role role = new Role(roleName, remark, roleType);
		roleDao.save(role);
		roleResourceAssignList.stream().forEach(roleResourceAssign -> roleResourceAssign.setRoleId(role.getId()));
		roleResourceAssignDao.saveBatch(roleResourceAssignList);
		return Result.rightResult();
	}

	@Override
	public Result modify(Long roleId, String roleName, String remark, List<RoleResourceAssign> roleResourceAssignList) {
		Role role = roleDao.findById(roleId);
		role.setRoleName(roleName);
		role.setRemark(remark);
		roleDao.update(role);
		roleResourceAssignDao.deleteByRoleId(roleId);
		roleResourceAssignDao.saveBatch(roleResourceAssignList);
		return Result.rightResult();
	}

	@Override
	public Result delete(Long roleId) {
		roleDao.deleteById(roleId);
		roleResourceAssignDao.deleteByRoleId(roleId);
		return Result.rightResult();
	}

	@Override
	public List<Role> findAll() {
		return roleDao.findAll();
	}
	
	@Override
	public List<Role> findUserRoles(Long userId) {
		return roleDao.findUserRoles(userId);
	}

}
