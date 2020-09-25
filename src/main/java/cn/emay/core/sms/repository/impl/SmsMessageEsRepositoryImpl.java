package cn.emay.core.sms.repository.impl;

import cn.emay.configuration.es.BasePojoEsRepository;
import cn.emay.core.sms.dto.SmsMessageDto;
import cn.emay.core.sms.dto.SmsMessageOperDto;
import cn.emay.core.sms.pojo.SmsMessage;
import cn.emay.core.sms.repository.SmsMessageEsRepository;
import cn.emay.utils.date.DateUtils;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.string.StringUtils;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class SmsMessageEsRepositoryImpl extends BasePojoEsRepository<SmsMessage> implements SmsMessageEsRepository {

    @Override
    public Page<SmsMessageOperDto> findPage(String batchNo, String appCode, String appKey, Long[] clientid, String content, String reportCode, String mobile,
                                            Integer state, String startTime, String endTime, int start, int limit, boolean isNextPage, Long startId) {
        StringBuilder sql = new StringBuilder();
        String ens = "G-SM-Cation-SM-G";
        sql.append("select * from ").append(ens).append(" where 1=1 ");
        if (StringUtils.isNotEmpty(batchNo)) {
            sql.append(" and batchNo = '").append(batchNo.trim()).append("' ");
        }
        if (StringUtils.isNotEmpty(appCode)) {
            sql.append(" and appCode = '").append(appCode.trim()).append("' ");
        }
        if (StringUtils.isNotEmpty(appKey)) {
            sql.append(" and appKey = '").append(appKey.trim()).append("' ");
        }
        if (clientid != null && clientid.length > 0) {
            sql.append(" and clientId in ( ").append(org.apache.commons.lang3.StringUtils.join(clientid, ",")).append(") ");
        }
        if (StringUtils.isNotEmpty(mobile)) {
            sql.append(" and mobile = '").append(mobile.trim()).append("' ");
        }
        if (state != null && state > -1) {
            sql.append(" and state = ").append(state).append(" ");
        }
        if (StringUtils.isNotEmpty(reportCode)) {
            sql.append(" and reportCode = '").append(reportCode).append("' ");
        }
        if (StringUtils.isNotEmpty(content)) {
            sql.append(" and content.key like '%").append(content.trim()).append("%' ");
        }
        if (StringUtils.isNotEmpty(startTime)) {
            sql.append(" and submitTime >= '").append(startTime.trim()).append("' ");
        }
        if (StringUtils.isNotEmpty(endTime)) {
            sql.append(" and submitTime <= '").append(endTime.trim()).append("' ");
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
        StringBuilder sql = new StringBuilder();
        String ens = "G-SM-Cation-SM-G";
        sql.append("select * from ").append(ens).append(" where 1=1 ");
        sql.append(" and clientId =  ").append(id).append(" ");
        if (StringUtils.isNotEmpty(appCode)) {
            sql.append(" and appCode = '").append(appCode).append("' ");
        }
        if (StringUtils.isNotEmpty(appKey)) {
            sql.append(" and appKey = '").append(appKey).append("' ");
        }
        if (StringUtils.isNotEmpty(mobile)) {
            sql.append(" and mobile = '").append(mobile).append("' ");
        }
        if (StringUtils.isNotEmpty(reportCode)) {
            sql.append(" and reportCode = '").append(reportCode).append("' ");
        }
        if (state != null && state > -1) {
            sql.append(" and state = ").append(state).append(" ");
        }
        if (StringUtils.isNotEmpty(content)) {
            sql.append(" and content.key like '%").append(content).append("%' ");
        }
        if (StringUtils.isNotEmpty(startDate)) {
            sql.append(" and submitTime >= '").append(startDate).append("' ");
        }
        if (StringUtils.isNotEmpty(endDate)) {
            sql.append(" and submitTime <= '").append(endDate).append("' ");
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
