package cn.emay.core.system.dao;

import cn.emay.core.system.pojo.Department;
import cn.emay.orm.BaseSuperDao;
import cn.emay.utils.db.common.Page;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Frank
 */
public interface DepartmentDao extends BaseSuperDao<Department> {

    /**
     * 根据用户ID批量查询用户所属部门名称
     *
     * @param userIds 用户IDs
     * @return 用户所属部门名称
     */
    Map<Long, String> findDepartmentNameByUserIds(Set<Long> userIds);

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
     * @param id             部门ID
     * @param departmentName 部门名称
     * @param start          起始
     * @param limit          数据条数
     * @return 部门
     */
    Page<Department> findPage(Long id, String departmentName, int start, int limit);

    /**
     * 根据ID批量查询部门
     *
     * @param ids id集合
     * @return 部门
     */
    List<Department> findByIds(List<Long> ids);

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
     * 查找子部门数量
     *
     * @param parentId 父级部门ID
     * @return 数量
     */
    Long findCountByParentId(Long parentId);

}
