package cn.emay.business.sms.api.client;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.emay.business.client.pojo.Client;
import cn.emay.business.sms.dto.SmsMessageDto;
import cn.emay.business.sms.repository.message.SmsMessageEsRepository;
import cn.emay.constant.web.ResourceEnum;
import cn.emay.constant.web.WebAuth;
import cn.emay.utils.WebUtils;
import cn.emay.utils.date.DateUtils;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.SuperResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 短信批次API
 *
 * @author chang
 */
@RestController
@Api(tags = {"短信"})
@RequestMapping(value = "/c/clientmessage", method = RequestMethod.POST)
public class ClientSmsMessageApi {

    @Autowired
    private SmsMessageEsRepository smsMessageEsRepository;

    /**
     * 短信列表
     *
     * @return
     */
    @WebAuth({ResourceEnum.CLIENT_MESSAGE_VIEW})
    @ApiOperation("短信列表")
    @RequestMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "appKey", value = "appKey", dataType = "string"),
            @ApiImplicitParam(name = "appCode", value = "appCode", dataType = "string"), @ApiImplicitParam(name = "content", value = "内容", dataType = "string"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "string"),
            @ApiImplicitParam(name = "state", value = "状态[1-待发送，2-发送中，3-已发送，4-发送成功,5-发送失败,6-未知(超时)]", dataType = "int"),
            @ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "date"), @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "date"),
            @ApiImplicitParam(name = "state", value = "状态", dataType = "int"), @ApiImplicitParam(name = "start", value = "开始位置", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "条数", dataType = "int"),
            @ApiImplicitParam(name = "startId", value = "开始ID，上一页的最后一行数据的ID", dataType = "string"),

    })
    public SuperResult<Page<SmsMessageDto>> list(String appCode, String appKey, String content, String reportCode, String mobile, Integer state,
                                                 String startDate, String endDate, int start, int limit, String pageType, String startId) {
    	if(content != null) {
    		content = content.replace(" ", "");
    	}
    	if(reportCode != null) {
    		reportCode = reportCode.replace(" ", "");
    	}
    	boolean isNextPage = true;
    	if(pageType != null && !pageType.equalsIgnoreCase("Next")) {
    		isNextPage = false;
    	}
    	Client client = WebUtils.getCurrentClient();
        if (startDate == null || endDate == null) {
            return SuperResult.badResult("开始结束时间不能为空");
        }
        startDate += " 000";
        endDate += " 999";

        Date startTime = DateUtils.parseDate(startDate, "yyyy-MM-dd HH:mm:ss SSS");
        Date endTIme = DateUtils.parseDate(endDate, "yyyy-MM-dd HH:mm:ss SSS");
        Date limitEnd = new Date();
        Date limitStart = DateUtils.parseDate("2019-12-01 00:00:00 000", "yyyy-MM-dd HH:mm:ss SSS");
        if (startTime.getTime() < limitStart.getTime()) {
            startDate = "2019-12-01 00:00:00 000";
        }
        if (endTIme.getTime() > limitEnd.getTime()) {
            endDate = DateUtils.toString(limitEnd, "yyyy-MM-dd HH:mm:ss SSS");
        }

        Long longStartId = null;
        if (startId != null) {
            longStartId = Long.parseLong(startId);
        }
        Page<SmsMessageDto> page = smsMessageEsRepository.findClientPage(appCode, appKey, client.getId(), content, reportCode, mobile, state, startDate,
                endDate, start, limit, isNextPage, longStartId);
        return SuperResult.rightResult(page);
    }

}
