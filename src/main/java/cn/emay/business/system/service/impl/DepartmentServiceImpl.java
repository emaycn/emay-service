package cn.emay.business.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.business.system.dao.DepartmentDao;
import cn.emay.business.system.dto.DepartmentDTO;
import cn.emay.business.system.pojo.Department;
import cn.emay.business.system.service.DepartmentService;
import cn.emay.utils.db.common.Page;

/**
 * 
 * @author Frank
 *
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

	@Resource
	DepartmentDao departmentDao;

	@Override
	public List<Department> findByParentId(Long parentId) {
		return departmentDao.findByParentId(parentId);
	}

	@Override
	public Department findByUserId(Long userId) {
		return departmentDao.findByUserId(userId);
	}

	@Override
	public Page<DepartmentDTO> findPage(Long parentId, String departmentName, int start, int limit) {
		Page<Department> page = departmentDao.findPage(parentId, departmentName, start, limit);
		Page<DepartmentDTO> pagedto = Page.createByStartAndLimit(start, limit, page.getTotalCount(), new ArrayList<DepartmentDTO>());
		if (page.getTotalCount() == 0) {
			return pagedto;
		}
		List<Long> list = new ArrayList<Long>();
		page.getList().stream().forEach(dep -> list.add(dep.getParentDepartmentId()));
		List<Department> depList = departmentDao.findByIds(list);
		Map<Long, String> map = new HashMap<Long, String>();
		depList.stream().forEach(depPar -> map.put(depPar.getId(), depPar.getDepartmentName()));
		page.getList().stream().forEach(dep -> pagedto.getList().add(new DepartmentDTO(dep, map.get(dep.getParentDepartmentId()))));
		return pagedto;
	}

	@Override
	public Boolean hasSameDepartmentNameAtParent(String departmentName, Long parentId, Long ignoreId) {
		return departmentDao.hasSameDepartmentNameAtParent(departmentName, parentId, ignoreId);
	}

	@Override
	public Department findDepartmentById(Long departmentId) {
		return departmentDao.findById(departmentId);
	}

	@Override
	public void addDepartment(Department department) {
		departmentDao.save(department);
	}

	@Override
	public Long findCountByParentId(Long parentId) {
		return departmentDao.findCountByParentId(parentId);
	}

	@Override
	public void deleteDepartment(Long departmentId) {
		departmentDao.deleteById(departmentId);
	}

	@Override
	public void modifyDepartment(Department department) {
		departmentDao.update(department);
	}

}