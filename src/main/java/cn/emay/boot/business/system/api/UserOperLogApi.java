package cn.emay.boot.business.system.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.emay.boot.base.constant.ResourceEnum;
import cn.emay.boot.base.web.WebAuth;
import cn.emay.boot.business.system.excel.UserOperLogExport;
import cn.emay.boot.business.system.excel.UserOperLogImport;
import cn.emay.boot.business.system.pojo.User;
import cn.emay.boot.business.system.pojo.UserOperLog;
import cn.emay.boot.business.system.service.UserOperLogService;
import cn.emay.boot.business.system.service.UserService;
import cn.emay.boot.config.PropertiesConfig;
import cn.emay.boot.utils.ApplicationContextUtils;
import cn.emay.boot.utils.FileUploadUtils;
import cn.emay.boot.utils.FileUploadUtils.FileUpLoadResult;
import cn.emay.boot.utils.WebUtils;
import cn.emay.excel.common.ExcelVersion;
import cn.emay.excel.read.ExcelReader;
import cn.emay.excel.write.ExcelWriter;
import cn.emay.utils.date.DateUtils;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.Result;
import cn.emay.utils.result.SuperResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 日志API
 * 
 * @author lijunjian
 *
 */
@RequestMapping(value = "/userlog", method = RequestMethod.POST)
@RestController
@Api(tags = { "日志管理" })
public class UserOperLogApi {

	Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserOperLogService userOperLogService;
	@Autowired
	private UserService userService;

	/**
	 * 日志列表
	 */
	@WebAuth({ ResourceEnum.LOG_VIEW })
	@RequestMapping("/page")
	@ApiOperation("分页查询日志列表")
	@ApiImplicitParams({ @ApiImplicitParam(name = "start", value = "起始数据位置", required = true, dataType = "int"), @ApiImplicitParam(name = "limit", value = "数据条数", required = true, dataType = "int"),
			@ApiImplicitParam(name = "username", value = "操作用户", dataType = "String"), @ApiImplicitParam(name = "realname", value = "用户姓名", dataType = "String"),
			@ApiImplicitParam(name = "content", value = "内容", dataType = "String"), @ApiImplicitParam(name = "startDate", value = "操作开始时间", dataType = "Date"),
			@ApiImplicitParam(name = "endDate", value = "操作结束时间", dataType = "Date"), })
	public SuperResult<Page<UserOperLog>> page(String username, String realname, String content, Date startDate, Date endDate, int start, int limit) {
		Page<UserOperLog> userpage = userOperLogService.findByPage(username, realname, content, startDate, endDate, start, limit);
		return SuperResult.rightResult(userpage);
	}

	/**
	 * 日志导入
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@WebAuth({ ResourceEnum.LOG_ADD })
	@RequestMapping(value = "/import", headers = "content-type=multipart/form-data")
	@ApiOperation("日志导入")
	public Result logImport(@ApiParam(value = "日志导入文件", required = true) MultipartFile file) throws Exception {
		String logDir = File.separator + "log" + File.separator + DateUtils.toString(new Date(), "yyyyMMdd") ;
		PropertiesConfig propertiesConfig = ApplicationContextUtils.getBean(PropertiesConfig.class);
		FileUpLoadResult result = FileUploadUtils.uploadFile(file, propertiesConfig.getUploadDirPath() + logDir, 5, ".xlsx", ".xlx");
		if (!result.isSuccess()) {
			return Result.badResult(result.getErrorMessage());
		}
		Map<String, User> userMap = new HashMap<String, User>();
		List<User> users = userService.findAll();
		users.stream().forEach(user -> userMap.put(user.getUsername(), user));
		List<UserOperLogImport> list = ExcelReader.readFirstSheet(result.getSaveFilePath(), UserOperLogImport.class);
		List<UserOperLog> logs = new ArrayList<>();
		list.stream().forEach(modle -> logs.add(modle.toUserOperLog(userMap.get(modle.getUsername()))));
		userOperLogService.savelogBatch(logs);
		User user = WebUtils.getCurrentUser();
		log.info("user : " + user.getUsername() + "导入日志 ");
		userOperLogService.saveLog("日志管理", user, "日志导入", UserOperLog.OPERATE_ADD);
		return Result.rightResult(logs.size());
	}

	/**
	 * 日志导出
	 */
	@WebAuth({ ResourceEnum.LOG_VIEW })
	@RequestMapping(value = "/export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, method = RequestMethod.GET)
	@ApiOperation("日志导出")
	@ApiImplicitParams({ @ApiImplicitParam(name = "username", value = "操作用户", dataType = "String"), @ApiImplicitParam(name = "realname", value = "用户姓名", dataType = "String"),
			@ApiImplicitParam(name = "content", value = "内容", dataType = "String"), @ApiImplicitParam(name = "startDate", value = "操作开始时间", dataType = "Date"),
			@ApiImplicitParam(name = "endDate", value = "操作结束时间", dataType = "Date"), })
	public void export(String username, String realname, String content, Date startDate, Date endDate) {
		// 查询1000行数据导出
		List<UserOperLog> datasAll = userOperLogService.findList(username, realname, content, startDate, endDate, 0, 1000);
		List<UserOperLogExport> list = new ArrayList<>();
		datasAll.forEach(log -> list.add(new UserOperLogExport(log)));
		HttpServletResponse response = WebUtils.getCurrentHttpResponse();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=log.xlsx");
		try {
			ExcelWriter.write(response.getOutputStream(), ExcelVersion.XLSX, list);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		User user = WebUtils.getCurrentUser();
		log.info("user : " + user.getUsername() + "导出日志 ");
	}

}
