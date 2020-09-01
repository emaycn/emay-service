package cn.emay.core.client.dao.impl;

import cn.emay.configuration.db.BasePojoSuperDaoImpl;
import cn.emay.core.client.dao.ClientUserDao;
import cn.emay.core.client.pojo.ClientUser;
import org.springframework.stereotype.Repository;

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