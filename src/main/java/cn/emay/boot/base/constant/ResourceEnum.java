package cn.emay.boot.base.constant;

public enum ResourceEnum {

	ROLE_VIEW("ROLE_VIEW", "角色查询"), //
	ROLE_ADD("ROLE_ADD", "角色添加"), //
	ROLE_DELETE("ROLE_DELETE", "角色删除"), //
	ROLE_MODIFY("ROLE_MODIFY", "角色编辑"), //

	USER_VIEW("USER_VIEW", "用户查询"), //
	USER_ADD("USER_ADD", "用户添加"), //
	USER_DELETE("USER_DELETE", "用户删除"), //
	USER_MODIFY("USER_MODIFY", "用户编辑"), //
	USER_OPER("USER_OPER", "用户操作"),//

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
