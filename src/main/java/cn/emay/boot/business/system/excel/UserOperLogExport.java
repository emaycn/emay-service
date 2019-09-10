package cn.emay.boot.business.system.excel;

import java.util.Date;

import cn.emay.boot.business.system.pojo.UserOperLog;
import cn.emay.excel.common.schema.annotation.ExcelColumn;
import cn.emay.excel.common.schema.annotation.ExcelSheet;

/**
 * 用户操作日志
 * 
 * @author lijunjian
 *
 */
@ExcelSheet(writeSheetName = "日志", isNeedBorder = true, contentRgbColor = { 220, 230, 241 }, titleRgbColor = { 250, 191, 143 })
public class UserOperLogExport {

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
	@ExcelColumn(index = 4, title = "操作时间", express = "yyyy-MM-dd HH:mm:ss")
	private Date operTime;
	/**
	 * 真实姓名
	 */
	@ExcelColumn(index = 5, title = "姓名")
	private String realname;

	public UserOperLogExport() {

	}

	public UserOperLogExport(UserOperLog log) {
		this.username = log.getUsername();
		this.realname = log.getRealname();
		this.module = log.getModule();
		this.content = log.getContent();
		this.operType = handleOperType(log.getOperType());
		this.operTime = log.getOperTime();
	}

	private String handleOperType(String operType) {
		String type = "";
		if (operType == null) {
			type = "查询";
		} else if (operType.equals(UserOperLog.OPERATE_ADD)) {
			type = "新增";
		} else if (operType.equals(UserOperLog.OPERATE_MODIFY)) {
			type = "修改";
		} else if (operType.equals(UserOperLog.OPERATE_DELETE)) {
			type = "删除";
		} else if (operType.equals(UserOperLog.OPERATE_SELECT)) {
			type = "查询";
		} else {
			type = "查询";
		}
		return type;
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

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

}
