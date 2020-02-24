package cn.emay.business.sms.repository.message;

import cn.emay.business.sms.dto.SmsMessageDto;
import cn.emay.business.sms.dto.SmsMessageOperDto;
import cn.emay.utils.db.common.Page;

public interface SmsMessageEsRepository {

	Page<SmsMessageOperDto> findPage(String batchNo, String appCode, String appKey, Long[] clientid, String content, String reportCode, String mobile, Integer state,
                                     String startTime, String endTime, int start, int limit, boolean isNextPage, Long startId);

	Page<SmsMessageDto> findClientPage(String appCode, String appKey, Long id, String content, String reportCode, String mobile, Integer state, String startDate, String endDate,
			int start, int limit, boolean isNextPage, Long startId);
}
