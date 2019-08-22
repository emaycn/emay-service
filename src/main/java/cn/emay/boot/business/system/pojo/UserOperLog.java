package cn.emay.boot.business.system.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户操作日志
 * @author lijunjian
 *
 */
@Entity
@Table(name="system_user_oper_log")
public class UserOperLog implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String OPERATE_ADD = "ADD";
	public static final String OPERATE_DELETE = "DELETE";
	public static final String OPERATE_MODIFY = "MODIFY";
	public static final String OPERATE_DOWNLOAD = "DOWNLOAD";
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;//
	
	@Column(name = "user_id")
	private Long userId;// 用户ID
	
	@Column(name = "user_name")
	private String username;//用户名
	
	@Column(name = "module")
	private String module;//操作模块
	
	@Column(name = "content")
	private String content;// 内容
	
	@Column(name = "oper_type")
	private String operType;// 操作类型[ADD-增，DELETE-删，MODIFY-改，DOWNLOAD-下载]
	
	@Column(name = "oper_time")
	private Date operTime;// 操作时间[yyyy-MM-dd HH:mm:ss]

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public Date getOperTime() {
		return operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
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
}
