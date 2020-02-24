package cn.emay.business.client.dao.impl;

import org.springframework.stereotype.Repository;

import cn.emay.base.dao.BasePojoSuperDaoImpl;
import cn.emay.business.client.dao.ClientUserDao;
import cn.emay.business.client.pojo.ClientUser;

/**
 * cn.emay.common.pojo.client.ClientUser Dao implement
 * 
 * @author chang
 */
@Repository
public class ClientUserDaoImpl extends BasePojoSuperDaoImpl<ClientUser> implements ClientUserDao {

    @Override
    public void deleteByUserId(Long userId) {
        this.deleteByProperty("user_id", userId);
    }
}