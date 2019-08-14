package cn.emay.boot.business.system.dao;

import java.util.List;

import cn.emay.boot.business.system.pojo.Department;
import cn.emay.orm.BaseSuperDao;
import cn.emay.utils.db.common.Page;

public interface DepartmentDao extends BaseSuperDao<Department>{

	Page<Department> findDepartmentByLikeName(Long id, String departmentName,Long enterpriseId,int start, int limit);
	
	List<Department> findByIds(List<Long> list);
	
	Long findCountByParentId(Long parentId);
	
	void deleteDepartment(Long departmentId);
	
	List<Department> findByParentId(Long parentId,Long enterpriseId);
	
	Long findDepartmentByName(String departmentName, Long id);
}