package cn.emay.core.system.service;

/**
 * @author lijunjian
 */
public interface UserDepartmentAssignService {

    /**
     * 查询部门下用户数量
     *
     * @param departmentId 部门ID
     * @return 数量
     */
    Long findByDepId(Long departmentId);

}
