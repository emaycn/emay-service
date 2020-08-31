package cn.emay.core.client.dao.impl;

import cn.emay.configuration.db.BasePojoSuperDaoImpl;
import cn.emay.core.client.dao.ClientChargeRecordDao;
import cn.emay.core.client.dto.ClientChargeRecordDTO;
import cn.emay.core.client.pojo.ClientChargeRecord;
import cn.emay.utils.db.common.Page;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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