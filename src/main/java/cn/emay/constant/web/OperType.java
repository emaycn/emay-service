package cn.emay.constant.web;

public enum OperType {

	ADD("ADD","新增"),
	DELETE("DELETE","删除"),
	MODIFY("MODIFY","修改"),
	OPER("OPER","操作"),
	
	;

	private String type;

	private String name;

	private OperType(String type, String name) {
		this.setType(type);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
