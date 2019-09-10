package cn.emay.boot.base.constant;

/**
 * 资源枚举值
 * 
 * @author Frank
 *
 */
public enum ResourceEnum {

	/**
	 * 角色查询
	 */
	ROLE_VIEW("ROLE_VIEW", "角色查询"),
	/**
	 * 角色添加
	 */
	ROLE_ADD("ROLE_ADD", "角色添加"),
	/**
	 * 角色删除
	 */
	ROLE_DELETE("ROLE_DELETE", "角色删除"),
	/**
	 * 角色修改
	 */
	ROLE_MODIFY("ROLE_MODIFY", "角色修改"),

	/**
	 * 用户查询
	 */
	USER_VIEW("USER_VIEW", "用户查询"),
	/**
	 * 用户添加
	 */
	USER_ADD("USER_ADD", "用户添加"),
	/**
	 * 用户删除
	 */
	USER_DELETE("USER_DELETE", "用户删除"),
	/**
	 * 用户修改
	 */
	USER_MODIFY("USER_MODIFY", "用户修改"),
	/**
	 * 用户操作
	 */
	USER_OPER("USER_OPER", "用户操作"),

	/**
	 * 部门查询
	 */
	DEPARTMENT_VIEW("DEPARTMENT_VIEW", "部门查询"),
	/**
	 * 部门添加
	 */
	DEPARTMENT_ADD("DEPARTMENT_ADD", "部门添加"),
	/**
	 * 部门删除
	 */
	DEPARTMENT_DELETE("DEPARTMENT_DELETE", "部门删除"),
	/**
	 * 部门修改
	 */
	DEPARTMENT_MODIFY("DEPARTMENT_MODIFY", "部门修改"),

	/**
	 * 日志查询
	 */
	LOG_VIEW("LOG_VIEW", "日志查询"),
	/**
	 * 日志添加
	 */
	LOG_ADD("LOG_ADD", "日志添加"),;

	/**
	 * 编码
	 */
	private String code;
	/**
	 * 描述
	 */
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
