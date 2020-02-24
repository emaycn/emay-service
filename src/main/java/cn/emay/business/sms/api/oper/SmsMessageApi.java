package cn.emay.business.sms.api.oper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.emay.business.client.pojo.Client;
import cn.emay.business.client.service.ClientService;
import cn.emay.business.sms.dto.SmsMessageOperDto;
import cn.emay.business.sms.repository.message.SmsMessageEsRepository;
import cn.emay.constant.web.ResourceEnum;
import cn.emay.constant.web.WebAuth;
import cn.emay.utils.date.DateUtils;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.SuperResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 短信详情API
 *
 * @author chang
 */
@RestController
@Api(tags = { "短信" })
@RequestMapping(value = "/o/message", method = RequestMethod.POST)
public class SmsMessageApi {

	@Autowired
	private SmsMessageEsRepository smsMessageEsRepository;
	@Autowired
	private ClientService clientService;

	/**
	 * 短信详情列表
	 *
	 * @return
	 */
	@WebAuth({ ResourceEnum.MESSAGE_VIEW })
	@ApiOperation("短信列表")
	@RequestMapping("/list")
	@ApiImplicitParams({ @ApiImplicitParam(name = "appKey", value = "appKey", dataType = "string"),
			@ApiImplicitParam(name = "appCode", value = "appCode", dataType = "string"), @ApiImplicitParam(name = "content", value = "内容", dataType = "string"),
			@ApiImplicitParam(name = "clientName", value = "客户名称", dataType = "string"), @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "string"),
			@ApiImplicitParam(name = "state", value = "状态[1-待发送，2-发送中，3-已发送，4-发送成功,5-发送失败,6-未知(超时)]", dataType = "int"),
			@ApiImplicitParam(name = "startDate", value = "开始时间", dataType = "String"),
			@ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "String"),
			@ApiImplicitParam(name = "reportCode", value = "状态码", dataType = "string"), @ApiImplicitParam(name = "batchNo", value = "批次号", dataType = "string"),
			@ApiImplicitParam(name = "start", value = "开始位置", dataType = "int"), @ApiImplicitParam(name = "limit", value = "条数", dataType = "int"),
			@ApiImplicitParam(name = "startId", value = "开始ID，上一页的最后一行数据的ID", dataType = "string"), })
	public SuperResult<Page<SmsMessageOperDto>> list(String batchNo, String appCode, String appKey, String clientName, String content, String reportCode,
			String mobile, Integer state, String startDate, String endDate, int start, int limit, String pageType, String startId) {
		if (content != null) {
			content = content.replace(" ", "");
		}
		if (reportCode != null) {
			reportCode = reportCode.replace(" ", "");
		}
		if (startDate == null || endDate == null) {
			return SuperResult.badResult("开始结束时间不能为空");
		}
		boolean isNextPage = true;
		if (pageType != null && !pageType.equalsIgnoreCase("Next")) {
			isNextPage = false;
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

		Long[] clientIds = getClientIds(clientName);
		if (StringUtils.isNotEmpty(clientName) && clientIds == null) {
			return SuperResult.rightResult(Page.createByStartAndLimit(start, limit, 0));
		}
		Long longStartId = null;
		if (startId != null) {
			longStartId = Long.parseLong(startId);
		}
		Page<SmsMessageOperDto> page = smsMessageEsRepository.findPage(batchNo, appCode, appKey, clientIds, content, reportCode, mobile, state, startDate,
				endDate, start, limit, isNextPage, longStartId);

		List<Long> clientIdList = new ArrayList<Long>();
		page.getList().forEach(message -> {
			clientIdList.add(message.getClientId());
		});
		Map<Long, Client> clientMap = getClientMap(clientIdList.toArray(new Long[clientIdList.size()]));
		page.getList().forEach(message -> {
			if(clientMap.containsKey(message.getClientId())) {
				message.setClientName(clientMap.get(message.getClientId()).getClientName());
			}
		});

		return SuperResult.rightResult(page);
	}

	/**
	 * 查询客户id
	 */
	private Long[] getClientIds(String clientName) {
		List<Long> clientId = new ArrayList<Long>();
		if (StringUtils.isNotEmpty(clientName)) {
			List<Client> clients = clientService.findByName(clientName);
			if (clients != null && clients.size() > 0) {
				clients.forEach(client -> clientId.add(client.getId()));
			}
		}
		return clientId.size() == 0 ? null : clientId.toArray(new Long[clientId.size()]);
	}

	/**
	 * 查询客户map
	 */
	private Map<Long, Client> getClientMap(Long[] clientIds) {
		Map<Long, Client> map = new HashMap<>();
		if (clientIds != null && clientIds.length > 0) {
			List<Client> apps = clientService.findbyIds(clientIds);
			apps.forEach(app -> map.put(app.getId(), app));
		}
		return map;
	}

}
