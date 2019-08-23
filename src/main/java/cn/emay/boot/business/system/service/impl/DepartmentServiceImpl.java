package cn.emay.boot.business.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.boot.business.system.dao.DepartmentDao;
import cn.emay.boot.business.system.dto.DepartmentDTO;
import cn.emay.boot.business.system.pojo.Department;
import cn.emay.boot.business.system.service.DepartmentService;
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
	public Page<DepartmentDTO> findDepartmentByLikeName(Long id, String departmentName, int start, int limit) {
		Page<Department> page = departmentDao.findDepartmentByLikeName(id, departmentName, start, limit);
		Page<DepartmentDTO> pagedto = new Page<DepartmentDTO>();
		List<DepartmentDTO> listdto = new ArrayList<DepartmentDTO>();
		List<Long> list = new ArrayList<Long>();
		if (page.getList().size() > 0) {
			for (Department dep : page.getList()) {
				list.add(dep.getParentDepartmentId());
			}
			List<Department> depList = departmentDao.findByIds(list);
			Map<Long, String> map = new HashMap<Long, String>();
			for (Department depPar : depList) {
				map.put(depPar.getId(), depPar.getDepartmentName());
			}
			for (Department dep : page.getList()) {
				DepartmentDTO departmentDTO = new DepartmentDTO(dep, map.get(dep.getParentDepartmentId()));
				listdto.add(departmentDTO);
			}
			pagedto.setList(listdto);
		} else {
			pagedto.setList(null);
		}
		pagedto.setCurrentPageNum(page.getCurrentPageNum());
		pagedto.setLimit(page.getLimit());
		pagedto.setStart(page.getStart());
		pagedto.setTotalCount(page.getTotalCount());
		pagedto.setTotalPage(page.getTotalPage());
		return pagedto;
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
		departmentDao.deleteDepartment(departmentId);
	}

	@Override
	public void modifyDepartment(Department department) {
		departmentDao.update(department);
	}

	@Override
	public List<Department> findByParentId(Long parentId) {
		return departmentDao.findByParentId(parentId);
	}

	@Override
	public Long findDepartmentByName(String departmentName, Long id) {
		return departmentDao.findDepartmentByName(departmentName, id);
	}

}
