package cn.emay.boot.business.system.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;

import cn.emay.boot.business.system.pojo.User;
import cn.emay.boot.business.system.pojo.UserOperLog;
import cn.emay.boot.business.system.service.ExcelReaderService;
import cn.emay.boot.utils.ApplicationContextUtils;
import cn.emay.boot.utils.CheckUtils;
import cn.emay.excel.read.reader.SheetReader;
import cn.emay.excel.utils.ExcelReadUtils;
import cn.emay.utils.string.StringUtils;

/**
 * 日志导入
 */
public class LogDataHandler implements SheetReader {

	private static ExcelReaderService excelReaderService = ApplicationContextUtils.getBean(ExcelReaderService.class);

	private List<Object[]> list = new ArrayList<Object[]>();
	private UserOperLog userOperLog;
	private Map<String, User> userMap;

	private static long size = 2000;
	private Map<String, Object> map = new HashMap<String, Object>();
	private List<String[]> errors = new ArrayList<String[]>();
	private Long succInsertLength = 0L;
	private Long count = 0L;

	public LogDataHandler() {

	}

	public LogDataHandler(Map<String, User> userMap) {
		this.userMap = userMap;
	}

	@Override
	public int getStartReadRowIndex() {
		return 0;
	}

	@Override
	public int getEndReadRowIndex() {
		return -1;
	}

	@Override
	public void begin(int sheetIndex, String sheetName) {

	}

	@Override
	public void beginRow(int rowIndex) {
		userOperLog = new UserOperLog();
	}

	@Override
	public void handleXlsCell(int rowIndex, int columnIndex, Cell cell) {
		switch (columnIndex) {
		case 0:
			userOperLog.setUsername(ExcelReadUtils.readString(cell));// 用户名
			break;
		case 1:
			userOperLog.setModule(ExcelReadUtils.readString(cell));// 操作模块
			break;
		case 2:
			userOperLog.setContent(ExcelReadUtils.readString(cell));// 内容
			break;
		case 3:
			userOperLog.setOperType(ExcelReadUtils.readString(cell));// 操作类型
			break;
		default:
			break;
		}
	}

	@Override
	public void handleXlsxCell(int rowIndex, int columnIndex, String value) {
		switch (columnIndex) {
		case 0:
			userOperLog.setUsername(ExcelReadUtils.readString(value));// 用户名
			break;
		case 1:
			userOperLog.setModule(ExcelReadUtils.readString(value));// 操作模块
			break;
		case 2:
			userOperLog.setContent(ExcelReadUtils.readString(value));// 内容
			break;
		case 3:
			userOperLog.setOperType(ExcelReadUtils.readString(value));// 操作类型
			break;
		default:
			break;
		}
	}

	@Override
	public void endRow(int rowIndex) {
		++count;
		if (null == userOperLog) {
			String[] array = { "", "", "", ""};
			errors.add(CheckUtils.copyOfRange(array, "不能为空"));
			return;
		}
		String checkData = checkData(userOperLog);
		if (!StringUtils.isEmpty(checkData)) {
			String[] array = { userOperLog.getUsername(), userOperLog.getModule(), userOperLog.getContent(), userOperLog.getOperType() };
			errors.add(CheckUtils.copyOfRange(array, checkData));
		} else {
			String operType = handleOperType(userOperLog.getOperType());
			list.add(new Object[] { userOperLog.getUserId(), userOperLog.getUsername(), userOperLog.getRealname(), userOperLog.getModule(), userOperLog.getContent(), operType});
			succInsertLength++;
		}
		if ((rowIndex + 1) % size == 0) {
			String sql = "insert into system_user_oper_log (user_id,username,realname,module,content,oper_type,oper_time) values(?,?,?,?,?,?,now())";
			excelReaderService.saveBatchPreparedStatement(sql, list);
			list.clear();
		}
	}

	@Override
	public void end(int sheetIndex, String sheetName) {

	}

	public String checkData(UserOperLog userOperLog) {
		if (StringUtils.isEmpty(userOperLog.getUsername())) {
			return "用户名不能为空";
		}
		if (!userMap.containsKey(userOperLog.getUsername())) {
			return "用户不存在";
		}
		User user = userMap.get(userOperLog.getUsername());
		if (user == null) {
			return "用户不存在";
		}
		userOperLog.setUserId(user.getId());
		userOperLog.setRealname(user.getRealname());
		if (StringUtils.isEmpty(userOperLog.getModule())) {
			return "操作模块不能为空";
		}
		if (StringUtils.isEmpty(userOperLog.getContent())) {
			return "内容不能为空";
		}
		if (StringUtils.isEmpty(userOperLog.getOperType())) {
			return "操作类型不能为空";
		}
		return null;
	}

	public void batchInsert() {
		if (!list.isEmpty()) {
			String sql = "insert into system_user_oper_log (user_id,username,realname,module,content,oper_type,oper_time) values(?,?,?,?,?,?,now())";
			excelReaderService.saveBatchPreparedStatement(sql, list);
			list.clear();
		}
	}

	public Map<String, Object> getState() {
		map.put("sum", count);
		map.put("errors", errors);
		map.put("fail", (errors.size()));
		map.put("success", succInsertLength);
		return map;
	}
	
	private String handleOperType(String operType) {
		String operTypeCode = "";
		if(operType.equals("新增")) {
			operTypeCode = UserOperLog.OPERATE_ADD;
		} else if(operType.equals("修改")) {
			operTypeCode = UserOperLog.OPERATE_MODIFY;
		} else if(operType.equals("删除")) {
			operTypeCode = UserOperLog.OPERATE_DELETE;
		} else if(operType.equals("查询")) {
			operTypeCode = UserOperLog.OPERATE_SELECT;
		}
		return operTypeCode;
	}
}
