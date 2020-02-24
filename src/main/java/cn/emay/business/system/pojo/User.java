package cn.emay.business.system.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 用户
 * 
 * @author Frank
 *
 */
@Entity
@Table(name = "system_user")
public class User implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 运营端用户-oper
	 */
	public final static String USER_FOR_OPER = "OPER";
	/**
	 * 客戶端用户-client
	 */
	public final static String USER_FOR_CLIENT = "CLIENT";

	/**
	 * 停用
	 */
	public final static int STATE_OFF = 0;
	/**
	 * 启用
	 */
	public final static int STATE_ON = 1;


	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 用户名
	 */
	@Column(name = "username")
	private String username;
	/**
	 * 密码
	 */
	@Column(name = "password")
	private String password;
	/**
	 * 真实姓名
	 */
	@Column(name = "realname")
	private String realname;
	/**
	 * 手机号
	 */
	@Column(name = "mobile")
	private String mobile;
	/**
	 * 邮箱
	 */
	@Column(name = "email")
	private String email;
	/**
	 * 状态[0-停用，1-启用]
	 */
	@Column(name = "user_state")
	private Integer userState;
	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	/**
	 * 创建者
	 */
	@Column(name = "operator_id")
	private Long operatorId;
	/**
	 * 最后一次修改密码时间[用来判断登陆修改密码]
	 */
	@Column(name = "last_change_password_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastChangePasswordTime;
	/**
	 * 所属系统[OPER-运营系统，CLIENT-客户系统]
	 */
	@Column(name = "user_for")
	private String userFor;

	public User() {

	}

	public User(String username, String password, String realname, String mobile, String email, Long operatorId, String userFor) {
		this.username = username;
		this.password = password;
		this.realname = realname;
		this.mobile = mobile;
		this.email = email;
		this.createTime = new Date();
		this.userState = STATE_ON;
		this.operatorId = operatorId;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getUserState() {
		return userState;
	}

	public void setUserState(Integer userState) {
		this.userState = userState;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public Date getLastChangePasswordTime() {
		return lastChangePasswordTime;
	}

	public void setLastChangePasswordTime(Date lastChangePasswordTime) {
		this.lastChangePasswordTime = lastChangePasswordTime;
	}

	public String getUserFor() {
		return userFor;
	}

	public void setUserFor(String userFor) {
		this.userFor = userFor;
	}

}
