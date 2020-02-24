package cn.emay.business.client.dao;

import java.util.Date;

import cn.emay.business.client.dto.ClientChargeRecordDTO;
import cn.emay.business.client.pojo.ClientChargeRecord;
import cn.emay.orm.BaseSuperDao;
import cn.emay.utils.db.common.Page;

/**
 * cn.emay.common.pojo.client.ClientChargeRecord Dao super
 * 
 * @author frank
 */
public interface ClientChargeRecordDao extends BaseSuperDao<ClientChargeRecord> {

    Page<ClientChargeRecordDTO> findChargePage(int start, int limit, String clientName, Date startTime, Date endTime);
}