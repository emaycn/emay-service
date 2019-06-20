package cn.emay.boot.business.system.dto;

import java.util.Date;

import cn.emay.boot.business.system.pojo.User;

public class UserDTO {

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

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 手机
	 */
	private String mobile;

	/**
	 * 状态
	 */
	private Integer state;

	/**
	 * 创建时间
	 */
	private Date createTime;

	public UserDTO(User user) {
		this.createTime = user.getCreateTime();
		this.email = user.getEmail();
		this.id = user.getId();
		this.mobile = user.getMobile();
		this.realname = user.getRealname();
		this.state = user.getState();
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

}
