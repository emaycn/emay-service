package cn.emay.core.client.dao;

import cn.emay.core.client.pojo.ClientUser;
import cn.emay.orm.BaseSuperDao;

/**
 * cn.emay.common.pojo.client.ClientUser Dao super
 *
 * @author chang
 */
public interface ClientUserDao extends BaseSuperDao<ClientUser> {
    /**
     * 根据用户id删除
     *
     * @param userId 用户Id
     * @return
     */
    void deleteByUserId(Long userId);
}