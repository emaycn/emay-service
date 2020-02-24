package cn.emay.business.sms.api.oper;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.emay.business.sms.dto.AppDto;
import cn.emay.business.sms.pojo.SmsApp;
import cn.emay.business.sms.service.SmsAppService;
import cn.emay.business.system.pojo.User;
import cn.emay.business.system.service.UserOperLogService;
import cn.emay.constant.web.OperType;
import cn.emay.constant.web.ResourceEnum;
import cn.emay.constant.web.WebAuth;
import cn.emay.utils.AppUtils;
import cn.emay.utils.WebUtils;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.Result;
import cn.emay.utils.result.SuperResult;
import cn.emay.utils.string.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 应用API
 *
 * @author chang
 */
@RestController
@Api(tags = { "应用" })
@RequestMapping(value = "/o/app", method = RequestMethod.POST)
public class AppApi {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private SmsAppService appService;
	@Autowired
	private UserOperLogService userOperLogService;

	/**
	 * 应用列表
	 *
	 * @return
	 */
	@WebAuth({ ResourceEnum.APP_VIEW })
	@ApiOperation("应用列表")
	@RequestMapping("/list")
	@ApiImplicitParams({ @ApiImplicitParam(name = "clientName", value = "客户名", dataType = "string"),
			@ApiImplicitParam(name = "appName", value = "应用名", dataType = "string"), @ApiImplicitParam(name = "appKey", value = "appKey", dataType = "string"),
			@ApiImplicitParam(name = "appCode", value = "服务号", dataType = "string"), @ApiImplicitParam(name = "state", value = "状态", dataType = "int"),
			@ApiImplicitParam(name = "start", value = "开始位置", dataType = "int"), @ApiImplicitParam(name = "limit", value = "条数", dataType = "int"), })
	public SuperResult<Page<AppDto>> list(int start, int limit, String clientName, String appName, String appKey, String appCode, Integer state) {
		Page<AppDto> appPage = appService.findPage(start, limit, clientName, appName, appKey, appCode, state);
		return SuperResult.rightResult(appPage);
	}

	/**
	 * 新增应用
	 */
	@WebAuth({ ResourceEnum.APP_ADD })
	@ApiOperation("新增应用")
	@RequestMapping("/add")
	@ApiImplicitParams({ @ApiImplicitParam(name = "clientId", value = "客户id", required = true, dataType = "long"),
			@ApiImplicitParam(name = "appName", value = "应用名称", required = true, dataType = "string"),
			@ApiImplicitParam(name = "isOptional", value = "是否自选", required = true, dataType = "int"),
			@ApiImplicitParam(name = "appCodeLength", value = "服务号位数", required = true, dataType = "int"),
			@ApiImplicitParam(name = "appCode", value = "服务号", dataType = "int"), @ApiImplicitParam(name = "remark", value = "备注", dataType = "string") })
	public SuperResult<SmsApp> add(Long clientId, String appName, Integer isOptional, Integer appCodeLength, String appCode, String remark) {
		if (null == clientId || clientId == 0L) {
			return SuperResult.badResult("客户不存在，请先创建客户！");
		}
		if (StringUtils.isEmpty(appName)) {
			return SuperResult.badResult("应用名称为空！");
		}
		// 生成或者检查服务号
		if (null != isOptional) {
			if (SmsApp.NOT_OPTIONAL == isOptional) {
				if (null != appCodeLength) {
					appCode = AppUtils.genAppCode(appCodeLength);
				}
			}
		} else {
			Result result = AppUtils.checkAppCode(appCode);
			if (!result.getSuccess()) {
				return SuperResult.badResult(result.getMessage());
			}
		}
		// 生成appkey和密钥
		String appKey = "";
		while (true) {
			appKey = AppUtils.genAppKey();
			SmsApp app = appService.findByAppKey(appKey);
			if (app == null) {
				break;
			}
		}
		String appSecret = AppUtils.genSecretKey();
		// 新增应用
		SmsApp app = new SmsApp(clientId, appName, appCode, SmsApp.APP_TYPE_SMS, appKey, appSecret, new BigDecimal("0.1"), SmsApp.STATE_OFF, remark);
		List<SmsApp> apps = new ArrayList<>();
		apps.add(app);
		appService.save(app);
		User user = WebUtils.getCurrentUser();
		String context = "新增应用:应用名为{0}";
		String module = "基础数据管理";
		userOperLogService.saveOperLog(module, MessageFormat.format(context, new Object[] { appName }), OperType.ADD);
		log.info("基础数据管理-->用户:" + user.getUsername() + "新增应用:应用为：" + appName);
		return SuperResult.rightResult(app);
	}

	/**
	 * 设置单价
	 */
	@WebAuth({ ResourceEnum.APP_PRICE })
	@ApiOperation("设置单价")
	@RequestMapping("/price")
	@ApiImplicitParams({ @ApiImplicitParam(name = "appId", value = "appId", required = true, dataType = "long"),
			@ApiImplicitParam(name = "price", value = "单价", required = true, dataType = "BigDecimal") })
	public SuperResult<String> price(Long appId, BigDecimal price) {
		if (null == appId || appId == 0L) {
			return SuperResult.badResult("appId有误！");
		}
		// 生成或者检查服务号
		SmsApp app = appService.findByAppId(appId);
		if (app == null) {
			return SuperResult.badResult("应用不存在！");
		}
		app.setPrice(price);
		appService.update(app);
		User user = WebUtils.getCurrentUser();
		String context = "设置单价:应用名为{0}";
		String module = "基础数据管理";
		userOperLogService.saveOperLog(module, MessageFormat.format(context, new Object[] { app.getAppName() }), OperType.MODIFY);
		log.info("基础数据管理-->用户:" + user.getUsername() + "设置单价:应用为：" + app.getAppName());
		return SuperResult.rightResult(app.getAppName());
	}


	/**
	 * 应用起停
	 */
	@WebAuth({ ResourceEnum.APP_ONOFF })
	@ApiOperation("应用起停")
	@RequestMapping("/onoff")
	@ApiImplicitParams({ @ApiImplicitParam(name = "appId", value = "appId", required = true, dataType = "long"),
			@ApiImplicitParam(name = "state", value = "状态", required = true, dataType = "int") })
	public SuperResult<String> onoff(Long appId, Integer state) {
		if (null == appId || appId == 0L) {
			return SuperResult.badResult("id有误！");
		}
		SmsApp app = appService.findByAppId(appId);
		if (app == null) {
			return SuperResult.badResult("应用不存在！");
		}
		User user = WebUtils.getCurrentUser();
		String context = "";
		String module = "基础数据管理";
		// 直接更新redis状态 KV_CHANNEL_CONTROLLER_ 循环判断状态是否更新成功HASH_CHANNEL_HEARTBEAT
		userOperLogService.saveOperLog(module, MessageFormat.format(context, new Object[] { app.getAppName() }), OperType.MODIFY);
		if (SmsApp.STATE_ON == state) {
			context = "应用起:应用名为{0}";
		} else if (SmsApp.STATE_OFF == state) {
			context = "应用停:应用名为{0}";
		}
		app.setState(state);
		appService.update(app);
		log.info("基础数据管理-->用户:" + user.getUsername() + context + app.getAppName());
		return SuperResult.rightResult(app.getAppName());
	}

}
