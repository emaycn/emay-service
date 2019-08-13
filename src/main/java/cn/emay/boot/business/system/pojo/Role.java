package cn.emay.boot.business.system.pojo;

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
 * 角色
 * 
 * @author Frank
 *
 */
@Entity
@Table(name = "system_role")
public class Role implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 角色名
	 */
	@Column(name = "role_name")
	private String roleName;

	@Column(name = "is_delete")
	private Boolean isDelete;
	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;

	public Role() {

	}

	public Role(String roleName, String remark, Boolean isDelete) {
		this.roleName = roleName;
		this.createTime = new Date();
		this.remark = remark;
		this.isDelete = isDelete;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

}
