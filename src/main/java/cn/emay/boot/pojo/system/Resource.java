package cn.emay.boot.pojo.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 资源<br/>
 * 多级资源，可自定义每级资源的定义<br/>
 * ag: 1-模块[无code,无path,无上级],2-导航[无code,无path],3-页面，4-操作[无icon]
 * 
 * @author Frank
 *
 */
@Entity
@Table(name = "system_resource")
public class Resource implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
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
	 * 资源路径
	 */
	@Column(name = "resource_path")
	private String resourcePath;
	/**
	 * 资源顺序
	 */
	@Column(name = "resource_index")
	private Integer resourceIndex;
	/**
	 * 资源图标
	 */
	@Column(name = "resource_icon")
	private String resourceIcon;
	/**
	 * 上级资源ID
	 */
	@Column(name = "parent_resource_id")
	private Long parentResourceId;

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

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public Integer getResourceIndex() {
		return resourceIndex;
	}

	public void setResourceIndex(Integer resourceIndex) {
		this.resourceIndex = resourceIndex;
	}

	public String getResourceIcon() {
		return resourceIcon;
	}

	public void setResourceIcon(String resourceIcon) {
		this.resourceIcon = resourceIcon;
	}

	public Long getParentResourceId() {
		return parentResourceId;
	}

	public void setParentResourceId(Long parentResourceId) {
		this.parentResourceId = parentResourceId;
	}

}
