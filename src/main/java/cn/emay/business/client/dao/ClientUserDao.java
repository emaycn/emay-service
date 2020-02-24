package cn.emay.business.client.dao;

import cn.emay.business.client.pojo.ClientUser;
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
     * @param userId  用户Id
     * @return
     */
    void deleteByUserId(Long userId);
}