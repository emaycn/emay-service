package cn.emay.boot.business.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.emay.boot.business.system.dao.RoleDao;
import cn.emay.boot.business.system.dao.RoleResourceAssignDao;
import cn.emay.boot.business.system.pojo.Role;
import cn.emay.boot.business.system.pojo.RoleResourceAssign;
import cn.emay.boot.business.system.service.RoleService;
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
	public Page<Role> findPage(int start, int limit, String roleName) {
		return roleDao.findPage(start, limit, roleName);
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
	public Result add(String roleName, String remark, List<RoleResourceAssign> roleResourceAssignList) {
		Role role = new Role(roleName, remark);
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
	
	/*---------------------------------*/
	


	



	

	

	@Override
	public List<Role> getUserRoles(Long userId) {
		return roleDao.getUserRoles(userId);
	}

	@Override
	public List<Role> findAll() {
		return roleDao.findAllRole();
	}

	

}
