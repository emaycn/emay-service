package cn.emay.boot.business.system.service;

import java.util.List;

import cn.emay.boot.business.system.dto.DepartmentDTO;
import cn.emay.boot.business.system.pojo.Department;
import cn.emay.utils.db.common.Page;


/**
 * 
* @项目名称：ebdp-web-operation 
* @类描述：部门管理   
* @创建人：lijunjian   
* @创建时间：2019年7月8日 上午9:56:31   
* @修改人：lijunjian   
* @修改时间：2019年7月8日 上午9:56:31   
* @修改备注：
 */
public interface DepartmentService {

	/**
	 * 根据名称模糊查找部门
	 */
	Page<DepartmentDTO> findDepartmentByLikeName(Long id, String departmentName,int start, int limit);
	
	/**
	 * 根据部门ID查找部门
	 */
	Department findDepartmentById(Long departmentId);
	
	/**
	 * 添加部门
	 */
	void addDepartment(Department department);
	
	/**
	 * 根据父类id查找部门数量
	 */
	Long findCountByParentId(Long parentId);
	
	/**
	 * 删除部门
	 */
	void deleteDepartment(Long departmentId);
	
	/**
	 * 修改部门
	 */
	void modifyDepartment(Department department);
	
	/**
	 * 根据父类id查找部门
	 */
	List<Department> findByParentId(Long parentId);
	
	/**
	 * 根据名称查找部门
	 */
	Long findDepartmentByName(String departmentName, Long id);
}
