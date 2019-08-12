package cn.emay.boot.base.constant;

public enum ResourceEnum {

	ROLE_VIEW("ROLE_VIEW", "角色管理"), //
	ROLE_ADD("ROLE_ADD", "角色添加"), //
	ROLE_DELETE("ROLE_DELETE", "角色删除"), //
	ROLE_MODIFY("ROLE_MODIFY", "角色修改"), //

	USER_VIEW("USER_VIEW", "用户管理"), //
	USER_ADD("USER_ADD", "用户添加"), //
	USER_DELETE("USER_DELETE", "用户删除"), //
	USER_MODIFY("USER_MODIFY", "用户修改"), //
	USER_OPER("USER_OPER", "用户操作"),//

	DEPARTMENT_VIEW("DEPARTMENT_VIEW", "部门管理"), //
	DEPARTMENT_ADD("DEPARTMENT_ADD", "部门添加"), //
	DEPARTMENT_DELETE("DEPARTMENT_DELETE", "部门删除"), //
	DEPARTMENT_MODIFY("DEPARTMENT_MODIFY", "部门修改"), //
	
	LOG_VIEW("LOG_VIEW", "日志管理"), //
	;

	private String code;

	private String desc;

	private ResourceEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
