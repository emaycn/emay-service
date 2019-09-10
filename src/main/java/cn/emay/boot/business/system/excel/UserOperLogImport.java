package cn.emay.boot.business.system.excel;

import java.util.Date;

import cn.emay.boot.business.system.pojo.User;
import cn.emay.boot.business.system.pojo.UserOperLog;
import cn.emay.excel.common.schema.annotation.ExcelColumn;
import cn.emay.excel.common.schema.annotation.ExcelSheet;

/**
 * 用户操作日志
 * 
 * @author lijunjian
 *
 */
@ExcelSheet(readDataStartRowIndex = 0)
public class UserOperLogImport {

	/**
	 * 用户名
	 */
	@ExcelColumn(index = 0, title = "用户名")
	private String username;
	/**
	 * 操作模块
	 */
	@ExcelColumn(index = 1, title = "操作模块")
	private String module;
	/**
	 * 内容
	 */
	@ExcelColumn(index = 2, title = "内容")
	private String content;
	/**
	 * 操作类型
	 */
	@ExcelColumn(index = 3, title = "操作类型")
	private String operType;
	/**
	 * 操作时间
	 */
	@ExcelColumn(index = 4, title = "操作时间")
	private Date operTime;

	public UserOperLog toUserOperLog(User user) {
		UserOperLog log = new UserOperLog();
		log.setContent(content);
		log.setModule(module);
		log.setOperTime(operTime == null ? new Date() : operTime);
		log.setOperType(handleOperType(operType));
		log.setRealname(user.getRealname());
		log.setUserId(user.getId());
		log.setUsername(username);
		return log;
	}

	private String handleOperType(String operType) {
		String operTypeCode = "";
		if ("新增".equals(operType)) {
			operTypeCode = UserOperLog.OPERATE_ADD;
		} else if ("修改".equals(operType)) {
			operTypeCode = UserOperLog.OPERATE_MODIFY;
		} else if ("删除".equals(operType)) {
			operTypeCode = UserOperLog.OPERATE_DELETE;
		} else if ("查询".equals(operType)) {
			operTypeCode = UserOperLog.OPERATE_SELECT;
		} else {
			operTypeCode = UserOperLog.OPERATE_SELECT;
		}
		return operTypeCode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public Date getOperTime() {
		return operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}

}
