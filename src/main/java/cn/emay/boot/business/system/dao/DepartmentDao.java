package cn.emay.boot.business.system.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.emay.boot.business.system.pojo.Department;
import cn.emay.orm.BaseSuperDao;
import cn.emay.utils.db.common.Page;

/**
 * 
 * @author Frank
 *
 */
public interface DepartmentDao extends BaseSuperDao<Department> {

	/**
	 * 根据用户ID批量查询用户所属部门名称
	 * 
	 * @param userIds
	 *            用户IDs
	 * @return
	 */
	Map<Long, String> findDepartmentNameByUserIds(Set<Long> userIds);

	/**
	 * 根据父类id查找部门
	 * 
	 * @param parentId
	 *            父级部门ID
	 * @return
	 */
	List<Department> findByParentId(Long parentId);

	/**
	 * 查询用户所属部门
	 * 
	 * @param userId
	 *            用户ID
	 * @return
	 */
	Department findByUserId(Long userId);

	/*---------------------------------*/

	Page<Department> findDepartmentByLikeName(Long id, String departmentName, int start, int limit);

	List<Department> findByIds(List<Long> list);

	Long findCountByParentId(Long parentId);

	void deleteDepartment(Long departmentId);

	Long findDepartmentByName(String departmentName, Long id);

}
