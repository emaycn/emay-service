package cn.emay.core.client.dao.impl;

import cn.emay.configuration.db.BasePojoSuperDaoImpl;
import cn.emay.core.client.dao.ClientDao;
import cn.emay.core.client.pojo.Client;
import cn.emay.utils.db.common.Page;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * cn.emay.common.pojo.client.Client Dao implement
 *
 * @author frank
 */
@Repository
public class ClientDaoImpl extends BasePojoSuperDaoImpl<Client> implements ClientDao {

    @Override
    public Page<Client> findPage(int start, int limit, String clientName, String linkman, String mobile) {
        String hql = "from Client where 1=1 ";
        Map<String, Object> params = new HashMap<String, Object>();
        if (!StringUtils.isEmpty(clientName)) {
            hql += " and clientName like :clientName ";
            params.put("clientName", "%" + clientName.trim() + "%");
        }
        if (!StringUtils.isEmpty(linkman)) {
            hql += " and linkman like :linkman ";
            params.put("linkman", "%" + linkman.trim() + "%");
        }
        if (!StringUtils.isEmpty(mobile)) {
            hql += " and mobile like :mobile ";
            params.put("mobile", "%" + mobile.trim() + "%");
        }
        hql += " order by id desc ";
        return this.getPageResult(hql, start, limit, params, Client.class);
    }

    @Override
    public Client findByUserId(Long userId) {
        List<Object> params = new ArrayList<>();
        String sql = "select c.* from client c,client_user_assign u where c.id=u.client_id and u.user_id=?";
        params.add(userId);
        return findObjectUnique(Client.class, sql, params.toArray());
    }

    @Override
    public List<Client> findByName(String clientName) {
        String sql = "select * from client where 1=1  ";
        List<String> params = new ArrayList<>();
        if (!StringUtils.isEmpty(clientName)) {
            clientName = "%" + clientName.trim() + "%";
            params.add(clientName);
            sql += " and client_name like ? ";
        }
        sql += " limit 10 ";
        return findObjectListByClass(Client.class, sql, params.toArray());
    }

    @Override
    public Client findByClientName(String clientName, Long clientId) {
        List<Object> params = new ArrayList<>();
        String sql = "select * from client where client_name = ?  ";
        params.add(clientName);
        if (clientId != null && clientId != -1L) {
            sql += " and id <> ? ";
            params.add(clientId);
        }
        sql += " limit 1";
        try {
            return findObjectUnique(Client.class, sql, params.toArray());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Map<Long, String> findClientNameByUserIds(Set<Long> userIds) {
        String hql = "select c.clientName,cu.userId FROM Client c,ClientUser cu where c.id=cu.clientId   and cu.userId in (:userIds) ";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userIds", userIds);
        List<Object[]> list = this.getListResult(Object[].class, hql, param);
        Map<Long, String> maps = new HashMap<>();
        for (Object[] oba : list) {
            String name = (String) oba[0];
            Long id = (Long) oba[1];
            maps.put(id, name);
        }
        return maps;
    }

    @Override
    public List<Client> findbyIds(Long[] clientIds) {
        String hql = "from Client where id in (:ids)  ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ids", clientIds);
        return this.getListResult(Client.class, hql, params);
    }

}