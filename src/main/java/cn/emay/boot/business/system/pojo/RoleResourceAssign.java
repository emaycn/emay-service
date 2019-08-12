package cn.emay.boot.business.system.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 角色资源绑定关系
 * 
 * @author Frank
 *
 */
@Entity
@Table(name = "system_role_resource_assign")
public class RoleResourceAssign implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 角色ID
	 */
	@Column(name = "role_id")
	private Long roleId;
	/**
	 * 资源ID
	 */
	@Column(name = "resource_id")
	private Long resourceId;

	public RoleResourceAssign() {

	}

	public RoleResourceAssign(Long roleId, Long resourceId) {
		this.roleId = roleId;
		this.resourceId = resourceId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

}
