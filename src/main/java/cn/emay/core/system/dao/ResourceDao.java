package cn.emay.core.system.dao;

import cn.emay.core.system.pojo.Resource;
import cn.emay.orm.BaseSuperDao;

import java.util.List;

/**
 * @author Frank
 */
public interface ResourceDao extends BaseSuperDao<Resource> {

    /**
     * 获取角色所有资源
     *
     * @param roleId 角色ID
     * @return 资源
     */
    List<Resource> getRoleResources(Long roleId);

    /**
     * 获取用户所有的资源
     *
     * @param userId 用户ID
     * @return 资源
     */
    List<Resource> getUserResources(Long userId);

    List<Resource> getByResourceType(String resourceType);

}
