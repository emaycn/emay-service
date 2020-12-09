package cn.emay.task;


import cn.emay.configuration.es.BasePojoEsRepository;
import cn.emay.core.sms.pojo.SmsMessage;
import cn.emay.superscheduler.core.SuperScheduled;
import cn.emay.utils.date.DateUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 定时创建index
 */
@Component
public class CreateEsIndexTask {

    @Resource
    private SmsMessageRes smsMessageRes;

    @SuperScheduled(fixedDelay = 3600000L)
    public void createEsIndex() {
        long nextMonth = System.currentTimeMillis() + 31L * 24L * 60L * 60L * 1000L;
        long before12Month = System.currentTimeMillis() - 12L * 31L * 24L * 60L * 60L * 1000L;
        List<String> months = DateUtils.findMonthBetweenDate(new Date(before12Month), new Date(nextMonth));
        months.forEach(smsMessageRes::createIndex);
    }

    @Bean
    public SmsMessageRes create() {
        return new SmsMessageRes();
    }

    public static class SmsMessageRes extends BasePojoEsRepository<SmsMessage> {

    }

}
