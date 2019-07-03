package cn.emay.boot.business.system.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 资源<br/>
 * 
 * @author Frank
 *
 */
@Entity
@Table(name = "system_resource")
public class Resource implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	public final static String RESOURCE_TYPE_MOUDLE = "MOUDLE";
	public final static String RESOURCE_TYPE_NAVIGATION = "NAVIGATION";
	public final static String RESOURCE_TYPE_PAGE = "PAGE";
	public final static String RESOURCE_TYPE_OPER = "OPER";

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
	 * 资源图标
	 */
	@Column(name = "resource_icon")
	private String resourceIcon;
	/**
	 * 资源地址
	 */
	@Column(name = "resource_url")
	private String resourceUrl;
	/**
	 * 是否外链
	 */
	@Column(name = "is_out_link")
	private Boolean isOutLink;
	/**
	 * 资源类型
	 */
	@Column(name = "resource_type")
	private String resourceType;
	/**
	 * 资源序号
	 */
	@Column(name = "resource_index")
	private Integer resourceIndex;
	/**
	 * 上级资源ID
	 */
	@Column(name = "parent_resource_id")
	private Long parentResourceId;
	
	public Resource() {
		
	}

	/**
	 * 测试专用
	 * @param id
	 * @param resourceCode
	 * @param parentResourceId
	 * @param resourceType
	 * @param resourceIndex
	 */
	@Deprecated
	public Resource(Long id,String resourceCode,  Long parentResourceId,String resourceType,Integer resourceIndex) {
		this.id = id;
		this.resourceCode = resourceCode;
		this.parentResourceId = parentResourceId;
		this.resourceType = resourceType;
		this.resourceIndex = resourceIndex;
	}

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

	public String getResourceIcon() {
		return resourceIcon;
	}

	public void setResourceIcon(String resourceIcon) {
		this.resourceIcon = resourceIcon;
	}

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	public Boolean getIsOutLink() {
		return isOutLink;
	}

	public void setIsOutLink(Boolean isOutLink) {
		this.isOutLink = isOutLink;
	}

	public Long getParentResourceId() {
		return parentResourceId;
	}

	public void setParentResourceId(Long parentResourceId) {
		this.parentResourceId = parentResourceId;
	}

	public Integer getResourceIndex() {
		return resourceIndex;
	}

	public void setResourceIndex(Integer resourceIndex) {
		this.resourceIndex = resourceIndex;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

}
