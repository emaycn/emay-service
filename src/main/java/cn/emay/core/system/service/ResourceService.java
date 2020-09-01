package cn.emay.core.system.service;

import cn.emay.core.system.pojo.Resource;

import java.util.List;

/**
 * @author lijunjian
 */
public interface ResourceService {

    /**
     * 获取所有资源
     *
     * @return
     */
    List<Resource> getAll();

    /**
     * 获取角色所有资源
     *
     * @param roleId 角色ID
     * @return
     */
    List<Resource> getRoleResources(Long roleId);

    /**
     * 获取用户所有的资源
     *
     * @param userId 用户ID
     * @return
     */
    List<Resource> getUserResources(Long userId);

    List<Resource> getByResourceType(String resourceType);

}
