package cn.emay.boot.business.system.api;

import java.io.BufferedOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.reflect.TypeToken;

import cn.emay.boot.base.constant.CommonConstant;
import cn.emay.boot.base.constant.ResourceEnum;
import cn.emay.boot.base.web.WebAuth;
import cn.emay.boot.business.system.handler.LogDataHandler;
import cn.emay.boot.business.system.handler.LogExportWriter;
import cn.emay.boot.business.system.pojo.User;
import cn.emay.boot.business.system.pojo.UserOperLog;
import cn.emay.boot.business.system.service.UserOperLogService;
import cn.emay.boot.business.system.service.UserService;
import cn.emay.boot.utils.CommonUtil;
import cn.emay.boot.utils.WebUtils;
import cn.emay.excel.common.ExcelVersion;
import cn.emay.excel.read.ExcelReader;
import cn.emay.excel.write.ExcelWriter;
import cn.emay.json.JsonHelper;
import cn.emay.redis.RedisClient;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.Result;
import cn.emay.utils.result.SuperResult;
import cn.emay.utils.string.StringUtils;
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
	private RedisClient redis;
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
		// 缓存
		int start = 0;
		int limit = 10000;
		Map<String, User> userMap = new HashMap<String, User>();
		while (true) {
			List<User> list = userService.findAllByPage(start, limit);
			if (list == null || list.isEmpty()) {
				break;
			}
			for (User user : list) {
				userMap.put(user.getUsername(), user);
			}
			start += limit;
		}
		Result result = CommonUtil.uploadExcel(file, "log", CommonConstant.FILE_MAX_SIZE);
		if (!result.getSuccess()) {
			return result;
		}
		String filePath = (String) result.getResult();
		User user = WebUtils.getCurrentUser();
		LogDataHandler readData = new LogDataHandler(userMap);
		ExcelReader.readFirstSheet(filePath, readData);
		readData.batchInsert();
		String downloadKey = user.getId() + UUID.randomUUID().toString();
		Map<String, Object> map = readData.getState();
		redis.set(downloadKey, map.get("errors"), 60 * 60 * 5);
		map.put("downloadKey", downloadKey);
		log.info("user : " + user.getUsername() + "导入日志 ");
		userOperLogService.saveLog("日志管理", user, "日志导入", UserOperLog.OPERATE_ADD);
		return Result.rightResult(map);
	}

	/**
	 * 错误信息导出
	 */
	@WebAuth({ ResourceEnum.LOG_ADD })
	@RequestMapping(value = "/exportError", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE, method = RequestMethod.GET)
	@ApiOperation("错误信息导出")
	@ApiImplicitParams({ @ApiImplicitParam(name = "downloadKey", value = "标识", required = true, dataType = "string"), })
	public void exportExcel(String downloadKey, HttpServletResponse response) {
		String value = redis.get(downloadKey);
		if (!StringUtils.isEmpty(value)) {
			// json解析
			List<String[]> errors = new ArrayList<String[]>();
			errors = JsonHelper.fromJson(new TypeToken<ArrayList<String[]>>() {
			}, value);
			String fileName = "logError";
			// response.setContentType("application/x-download");
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".txt");
			BufferedOutputStream buff = null;
			StringBuffer write = new StringBuffer();
			String enter = "\r\n";
			write.append("错误信息");
			write.append(enter);
			ServletOutputStream outSTr = null;
			try {
				outSTr = response.getOutputStream(); // 建立
				buff = new BufferedOutputStream(outSTr);
				// 把内容写入文件
				if (errors.size() > 0) {
					for (int i = 0; i < errors.size(); i++) {
						write.append(Arrays.toString(errors.get(i)));
						write.append(enter);
					}
				}
				buff.write(write.toString().getBytes("UTF-8"));
				buff.flush();
				buff.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					buff.close();
					outSTr.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 日志导出
	 */
	@WebAuth({ ResourceEnum.LOG_VIEW })
	@RequestMapping("/export")
	@ApiOperation("日志导出")
	@ApiImplicitParams({ @ApiImplicitParam(name = "username", value = "操作用户", dataType = "String"), @ApiImplicitParam(name = "realname", value = "用户姓名", dataType = "String"),
			@ApiImplicitParam(name = "content", value = "内容", dataType = "String"), @ApiImplicitParam(name = "startDate", value = "操作开始时间", dataType = "Date"),
			@ApiImplicitParam(name = "endDate", value = "操作结束时间", dataType = "Date"), })
	public void export(String username, String realname, String content, Date startDate, Date endDate) {
		String[] array = { "操作内容", "服务模块", "操作类型", "操作用户", "操作用户姓名","操作时间" };
		List<String> titles = new ArrayList<>(Arrays.asList(array));
		String sheetName = "日志";
		int start = 0;
		int limit = 1000;
		List<UserOperLog> datasAll = new ArrayList<UserOperLog>();
		while (true) {
			Page<UserOperLog> page = userOperLogService.findByPage(username, realname, content, startDate, endDate, start, limit);
			if (null == page || page.getList() == null || page.getList().isEmpty()) {
				break;
			}
			List<UserOperLog> datas = page.getList();
			datasAll.addAll(datas);
			start += limit;
		}

		HttpServletResponse response = WebUtils.getCurrentHttpResponse();
		// response.setContentType("application/x-download");
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=log.xlsx");
		BufferedOutputStream buff = null;
		ServletOutputStream outSTr = null;
		int cacheNum = 1000;
		try {
			outSTr = response.getOutputStream();
			buff = new BufferedOutputStream(outSTr);
			ExcelWriter.write(buff, ExcelVersion.XLSX, cacheNum,new LogExportWriter(titles,sheetName,datasAll));
			User user = WebUtils.getCurrentUser();
			log.info("user : " + user.getUsername() + "导出日志 ");
			userOperLogService.saveLog("日志管理", user, "日志导出", UserOperLog.OPERATE_SELECT);
			buff.flush();
			buff.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buff.close();
				outSTr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
