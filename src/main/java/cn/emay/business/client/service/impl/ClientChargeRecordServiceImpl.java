package cn.emay.business.client.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.business.client.dao.ClientChargeRecordDao;
import cn.emay.business.client.dto.ClientChargeRecordDTO;
import cn.emay.business.client.pojo.ClientChargeRecord;
import cn.emay.business.client.service.ClientChargeRecordService;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.Result;

/**
 * cn.emay.common.pojo.client.ClientChargeRecord Service implement
 * 
 * @author frank
 */
@Service
public class ClientChargeRecordServiceImpl implements ClientChargeRecordService {

	@Resource
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