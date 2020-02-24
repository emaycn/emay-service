package cn.emay.business.system.dto;

import java.io.Serializable;

import cn.emay.business.system.pojo.User;

/**
 * 用戶详情DTO
 * 
 * @author Frank
 *
 */
public class UserInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private Long id;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 真实姓名
	 */
	private String realname;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 状态[0-删除，1-停用，2-启用]
	 */
	private Integer userState;
	/**
	 * 所属系统[OPER-运营系统，CLIENT-客户系统]
	 */
	private String userFor;


	public UserInfoDTO() {

	}

	public UserInfoDTO(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.realname = user.getRealname();
		this.mobile = user.getMobile();
		this.email = user.getEmail();
		this.userState = user.getUserState();
		this.userFor = user.getUserFor();
	}

	public String getUserFor() {
		return userFor;
	}

	public void setUserFor(String userFor) {
		this.userFor = userFor;
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

	public Integer getUserState() {
		return userState;
	}

	public void setUserState(Integer userState) {
		this.userState = userState;
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

}
