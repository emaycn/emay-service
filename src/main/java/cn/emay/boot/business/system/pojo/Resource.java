package cn.emay.boot.business.system.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
* @项目名称：ebdp-web-operation 
* @类描述：资源表   
* @创建人：lijunjian   
* @创建时间：2019年7月30日 上午10:25:23   
* @修改人：lijunjian   
* @修改时间：2019年7月30日 上午10:25:23   
* @修改备注：
 */
@Entity
@Table(name = "system_resource")
public class Resource implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

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
}
