package cn.emay.core.client.service.impl;

import cn.emay.core.client.dao.ClientChargeRecordDao;
import cn.emay.core.client.dto.ClientChargeRecordDTO;
import cn.emay.core.client.pojo.ClientChargeRecord;
import cn.emay.core.client.service.ClientChargeRecordService;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * cn.emay.common.pojo.client.ClientChargeRecord Service implement
 *
 * @author frank
 */
@Service
public class ClientChargeRecordServiceImpl implements ClientChargeRecordService {

    @Autowired
    private ClientChargeRecordDao clientChargeRecordDao;

    @Override
    public Page<ClientChargeRecordDTO> findChargePage(int start, int limit, String clientName, Date startTime, Date endTime) {
        return clientChargeRecordDao.findChargePage(start, limit, clientName, startTime, endTime);
    }

    @Override
    public Result add(ClientChargeRecord clientChargeRecord) {
        clientChargeRecordDao.save(clientChargeRecord);
        return Result.rightResult();
    }
}