package cn.emay.core.client.dao;

import cn.emay.core.client.dto.ClientChargeRecordDTO;
import cn.emay.core.client.pojo.ClientChargeRecord;
import cn.emay.orm.BaseSuperDao;
import cn.emay.utils.db.common.Page;

import java.util.Date;

/**
 * cn.emay.common.pojo.client.ClientChargeRecord Dao super
 *
 * @author frank
 */
public interface ClientChargeRecordDao extends BaseSuperDao<ClientChargeRecord> {

    Page<ClientChargeRecordDTO> findChargePage(int start, int limit, String clientName, Date startTime, Date endTime);
}