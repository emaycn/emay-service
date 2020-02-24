package cn.emay.business.sms.repository.message.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.gson.reflect.TypeToken;

import cn.emay.base.repository.BasePojoEsRepository;
import cn.emay.business.sms.dto.SmsMessageDto;
import cn.emay.business.sms.dto.SmsMessageOperDto;
import cn.emay.business.sms.pojo.SmsMessage;
import cn.emay.business.sms.repository.message.SmsMessageEsRepository;
import cn.emay.utils.date.DateUtils;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.string.StringUtils;

@Service
public class SmsMessageEsRepositoryImpl extends BasePojoEsRepository<SmsMessage> implements SmsMessageEsRepository {

	@Override
	public Page<SmsMessageOperDto> findPage(String batchNo, String appCode, String appKey, Long[] clientid, String content, String reportCode, String mobile,
			Integer state, String startTime, String endTime, int start, int limit, boolean isNextPage, Long startId) {
		StringBuffer sql = new StringBuffer();
		String ens = "G-SM-Cation-SM-G";
		sql.append("select * from " + ens + " where 1=1 ");
		if (StringUtils.isNotEmpty(batchNo)) {
			sql.append(" and batchNo = '" + batchNo.trim() + "' ");
		}
		if (StringUtils.isNotEmpty(appCode)) {
			sql.append(" and appCode = '" + appCode.trim() + "' ");
		}
		if (StringUtils.isNotEmpty(appKey)) {
			sql.append(" and appKey = '" + appKey.trim() + "' ");
		}
		if (clientid != null && clientid.length > 0) {
			sql.append(" and clientId in ( " + org.apache.commons.lang3.StringUtils.join(clientid, ",") + ") ");
		}
		if (StringUtils.isNotEmpty(mobile)) {
			sql.append(" and mobile = '" + mobile.trim() + "' ");
		}
		if (state != null && state > -1) {
			sql.append(" and state = " + state + " ");
		}
		if (StringUtils.isNotEmpty(reportCode)) {
			sql.append(" and reportCode = '" + reportCode + "' ");
		}
		if (StringUtils.isNotEmpty(content)) {
			sql.append(" and content.key like '%" + content.trim() + "%' ");
		}
		if (StringUtils.isNotEmpty(startTime)) {
			sql.append(" and submitTime >= '" + startTime.trim() + "' ");
		}
		if (StringUtils.isNotEmpty(endTime)) {
			sql.append(" and submitTime <= '" + endTime.trim() + "' ");
		}
		sql.append(" order by id desc ");
		String[] indexs = findBetweenDate(startTime, endTime, this.getIndexName());
		int count = 0;
		List<String> indexNames = new ArrayList<>();
		int limitHas = 0;
		for (String index : indexs) {
			String sqlnew = sql.toString().replace(ens, index);
			count += this.queryCount(sqlnew);
			if (start < count && limitHas < limit) {
				indexNames.add(index);
				limitHas = count - start;
			}
		}
		List<SmsMessageOperDto> list = new ArrayList<>();
		boolean isNeedStart = true;
		for (String indexname : indexNames) {
			if (!isNeedStart) {
				startId = null;
			}
			String sqlnew = sql.toString().replace(ens, indexname);
			List<SmsMessageOperDto> result = queryListForPage(sqlnew, isNextPage, startId, limit, new TypeToken<List<SmsMessageOperDto>>() {
			});
			list.addAll(result);
			isNeedStart = false;
		}
		if (list.size() > limit) {
			list = list.subList(0, limit);
		}
		Page<SmsMessageOperDto> page = new Page<>();
		page.setNumByStartAndLimit(start, limit, count);
		page.setList(list);
		return page;
	}

	@Override
	public Page<SmsMessageDto> findClientPage(String appCode, String appKey, Long id, String content, String reportCode, String mobile, Integer state,
			String startDate, String endDate, int start, int limit, boolean isNextPage, Long startId) {
		StringBuffer sql = new StringBuffer();
		String ens = "G-SM-Cation-SM-G";
		sql.append("select * from " + ens + " where 1=1 ");
		sql.append(" and clientId =  " + id + " ");
		if (StringUtils.isNotEmpty(appCode)) {
			sql.append(" and appCode = '" + appCode + "' ");
		}
		if (StringUtils.isNotEmpty(appKey)) {
			sql.append(" and appKey = '" + appKey + "' ");
		}
		if (StringUtils.isNotEmpty(mobile)) {
			sql.append(" and mobile = '" + mobile + "' ");
		}
		if (StringUtils.isNotEmpty(reportCode)) {
			sql.append(" and reportCode = '" + reportCode + "' ");
		}
		if (state != null && state > -1) {
			sql.append(" and state = " + state + " ");
		}
		if (StringUtils.isNotEmpty(content)) {
			sql.append(" and content.key like '%" + content + "%' ");
		}
		if (StringUtils.isNotEmpty(startDate)) {
			sql.append(" and submitTime >= '" + startDate + "' ");
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sql.append(" and submitTime <= '" + endDate + "' ");
		}
		sql.append(" order by id desc ");

		String[] indexs = findBetweenDate(startDate, endDate, this.getIndexName());
		int count = 0;
		List<String> indexNames = new ArrayList<>();
		int limitHas = 0;
		for (String index : indexs) {
			String sqlnew = sql.toString().replace(ens, index);
			count += this.queryCount(sqlnew);
			if (start < count && limitHas < limit) {
				indexNames.add(index);
				limitHas = count - start;
			}
		}

		List<SmsMessageDto> list = new ArrayList<>();
		boolean isNeedStart = true;
		for (String indexname : indexNames) {
			if (!isNeedStart) {
				startId = null;
			}
			String sqlnew = sql.toString().replace(ens, indexname);
			List<SmsMessageDto> result = queryListForPage(sqlnew, isNextPage, startId, limit, new TypeToken<List<SmsMessageDto>>() {
			});
			list.addAll(result);
			isNeedStart = false;
		}
		if (list.size() > limit) {
			list = list.subList(0, limit);
		}
		Page<SmsMessageDto> page = new Page<>();
		page.setNumByStartAndLimit(start, limit, count);
		page.setList(list);
		return page;
	}

	public String[] findBetweenDate(String beginDate, String endDate, String indexName) {
		Date beginTime = DateUtils.parseDate(beginDate, "yyyy-MM-dd HH:mm:ss SSS");
		Date endTime = DateUtils.parseDate(endDate, "yyyy-MM-dd HH:mm:ss SSS");
		List<String> retList = DateUtils.findMonthBetweenDate(beginTime, endTime);
		Collections.reverse(retList);
		String[] indexNames = new String[retList.size()];
		for (int i = 0; i < retList.size(); i++) {
			indexNames[i] = this.getIndexName(retList.get(i));
		}
		return indexNames;
	}

}
