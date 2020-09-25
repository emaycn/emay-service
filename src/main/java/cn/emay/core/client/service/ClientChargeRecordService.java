package cn.emay.core.client.service;

import cn.emay.core.client.dto.ClientChargeRecordDTO;
import cn.emay.core.client.pojo.ClientChargeRecord;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.Result;

import java.util.Date;

/**
 * cn.emay.common.pojo.client.ClientChargeRecord Service Super
 *
 * @author frank
 */
public interface ClientChargeRecordService {

    /**
     * 客户账务列表<br/>
     *
     * @param start      开始页数
     * @param limit      条数
     * @param clientName 客户名
     * @param endTime    结束时间
     * @param startTime  开始时间
     * @return 分页数据
     */
    Page<ClientChargeRecordDTO> findChargePage(int start, int limit, String clientName, Date startTime, Date endTime);

    /**
     * 保存充值扣费记录<br/>
     *
     * @param clientChargeRecord 充值扣费记录
     * @return 添加结果
     */
    Result add(ClientChargeRecord clientChargeRecord);
}