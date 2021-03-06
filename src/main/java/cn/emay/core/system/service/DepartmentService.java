package cn.emay.core.system.service;

import cn.emay.core.system.dto.DepartmentDTO;
import cn.emay.core.system.pojo.Department;
import cn.emay.utils.db.common.Page;

import java.util.List;

/**
 * @author lijunjian
 */
public interface DepartmentService {

    /**
     * 根据父类id查找部门
     *
     * @param parentId 父级部门ID
     * @return 部门
     */
    List<Department> findByParentId(Long parentId);

    /**
     * 查询用户所属部门
     *
     * @param userId 用户ID
     * @return 部门
     */
    Department findByUserId(Long userId);

    /**
     * 查询父级部门下的部门列表
     *
     * @param parentId       父级部门ID
     * @param departmentName 部门名称
     * @param start          起始
     * @param limit          数据条数
     * @return 部门
     */
    Page<DepartmentDTO> findPage(Long parentId, String departmentName, int start, int limit);

    /**
     * 查找同级是否有相同的部门名
     *
     * @param departmentName 部门名称
     * @param parentId       父级ID
     * @param ignoreId       忽略对比的ID
     * @return 同级是否有相同的部门名
     */
    Boolean hasSameDepartmentNameAtParent(String departmentName, Long parentId, Long ignoreId);

    /**
     * 根据部门ID查找部门
     *
     * @param departmentId 部门ID
     * @return 部门
     */
    Department findDepartmentById(Long departmentId);

    /**
     * 添加部门
     *
     * @param department 部门
     */
    void addDepartment(Department department);

    /**
     * 查找子部门数量
     *
     * @param parentId 父级部门ID
     * @return 数量
     */
    Long findCountByParentId(Long parentId);

    /**
     * 删除部门
     *
     * @param departmentId 部门ID
     */
    void deleteDepartment(Long departmentId);

    /**
     * 修改部门
     *
     * @param department 部门
     */
    void modifyDepartment(Department department);

}
