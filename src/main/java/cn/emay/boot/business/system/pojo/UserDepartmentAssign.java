package cn.emay.boot.business.system.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @项目名称：ebdp-web-operation  @类描述：用户部门   @创建人：lijunjian  
 * 
 * @创建时间：2019年7月30日 上午11:01:23   @修改人：lijunjian  
 * @修改时间：2019年7月30日 上午11:01:23   @修改备注：
 */
@Entity
@Table(name = "system_user_department_assign")
public class UserDepartmentAssign implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 部门ID
	 */
	@Column(name = "system_department_id")
	private Long systemDepartmentId;

	/**
	 * 用户ID
	 */
	@Column(name = "system_user_id")
	private Long systemUserId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSystemDepartmentId() {
		return systemDepartmentId;
	}

	public void setSystemDepartmentId(Long systemDepartmentId) {
		this.systemDepartmentId = systemDepartmentId;
	}

	public Long getSystemUserId() {
		return systemUserId;
	}

	public void setSystemUserId(Long systemUserId) {
		this.systemUserId = systemUserId;
	}
}
