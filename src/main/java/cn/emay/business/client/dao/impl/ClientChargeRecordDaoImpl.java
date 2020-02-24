package cn.emay.business.client.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.emay.base.dao.BasePojoSuperDaoImpl;
import cn.emay.business.client.dao.ClientChargeRecordDao;
import cn.emay.business.client.dto.ClientChargeRecordDTO;
import cn.emay.business.client.pojo.ClientChargeRecord;
import cn.emay.utils.db.common.Page;

/**
 * cn.emay.common.pojo.client.ClientChargeRecord Dao implement
 * 
 * @author frank
 */
@Repository
public class ClientChargeRecordDaoImpl extends BasePojoSuperDaoImpl<ClientChargeRecord> implements ClientChargeRecordDao {

    @Override
    public Page<ClientChargeRecordDTO> findChargePage(int start, int limit, String clientName, Date startTime, Date endTime) {
        String sql = "select r.*,c.client_name ,u.realname as charge_user_name  from client c ,system_user u, client_charge_record r where u.id=r.charge_user_id and c.id=r.client_id  ";
        List<Object> parameters = new ArrayList<Object>();
        if (!StringUtils.isEmpty(clientName)) {
            sql += " and c.client_name like ? ";
            parameters.add("%" + clientName.trim() + "%");
        }
        if (startTime != null) {
            sql += " and r.create_time >=? ";
            parameters.add(startTime);
        }
        if (endTime != null) {
            sql += " and r.create_time <=? ";
            parameters.add(endTime);
        }
        sql += " order by r.id desc ";
        return findObjectPageByClassInMysql(ClientChargeRecordDTO.class, sql, start, limit, parameters.toArray());
    }
}