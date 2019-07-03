package cn.emay.boot.business.system.dto;

import java.io.Serializable;

import cn.emay.boot.business.system.pojo.User;

public class SimpleUserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private Long id;

	/**
	 * 姓名
	 */
	private String realname;

	/**
	 * 用户名
	 */
	private String username;

	public SimpleUserDTO() {

	}

	public SimpleUserDTO(User user) {
		this.id = user.getId();
		this.realname = user.getRealname();
		this.username = user.getUsername();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

}
