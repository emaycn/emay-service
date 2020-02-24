package cn.emay.business.system.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 资源
 * 
 * @author frank
 *
 */
@Entity
@Table(name = "system_resource")
public class Resource implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 资源类型[OPER-运营端，CLIENT客户端]-oper
	 */
	public final static String RESOURCE_TYPE_OPER = "OPER";
	/**
	 * 资源类型[OPER-运营端，CLIENT客户端]-client
	 */
	public final static String RESOURCE_TYPE_CLIENT = "CLIENT";

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * 资源CODE
	 */
	@Column(name = "resource_code")
	private String resourceCode;
	/**
	 * 资源名称
	 */
	@Column(name = "resource_name")
	private String resourceName;
	/**
	 * 资源类型
	 */
	@Column(name = "resource_type")
	private String resourceType;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getResourceCode() {
		return resourceCode;
	}

	public void setResourceCode(String resourceCode) {
		this.resourceCode = resourceCode;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
}
