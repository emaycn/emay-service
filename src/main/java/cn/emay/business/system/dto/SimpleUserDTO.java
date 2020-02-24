package cn.emay.business.system.dto;

import java.io.Serializable;
import java.util.Date;

import cn.emay.business.system.pojo.User;

/**
 * 用户简易DTO
 * 
 * @author Frank
 *
 */
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

	/**
	 * 最后一次修改密码时间
	 */
	private Date lastChangePasswordTime;

	public SimpleUserDTO() {

	}

	public SimpleUserDTO(User user) {
		this.id = user.getId();
		this.realname = user.getRealname();
		this.username = user.getUsername();
		this.lastChangePasswordTime = user.getLastChangePasswordTime();
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

	@Override
	public String toString() {
		return super.toString();
	}

	public Date getLastChangePasswordTime() {
		return lastChangePasswordTime;
	}

	public void setLastChangePasswordTime(Date lastChangePasswordTime) {
		this.lastChangePasswordTime = lastChangePasswordTime;
	}

}
