package cn.emay.boot.business.system.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
* @项目名称：emay-spring-boot 
* @类描述：部门   
* @创建人：lijunjian   
* @创建时间：2019年8月12日 上午10:26:06   
* @修改人：lijunjian   
* @修改时间：2019年8月12日 上午10:26:06   
* @修改备注：
 */
@Entity
@Table(name = "system_enterprise_department")
@ApiModel(value = "Department", description = "部门对象")
public class Department implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public final static Long MANAGE_ENTERPRISEID=0L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "department_name")
	@ApiModelProperty(value = "部门名称")
	private String departmentName;// 部门名称

	@Column(name = "department_code")
	@ApiModelProperty(value = "部门编码")
	private String departmentCode;// 部门编码

	@Column(name = "remark")
	@ApiModelProperty(value = "备注")
	private String remark;// 备注

	@Column(name = "full_path")
	@ApiModelProperty(value = "部门ID全路径")
	private String fullPath;// 部门ID全路径

	@Column(name = "enterprise_id")
	@ApiModelProperty(value = "所属企业ID")
	private Long enterpriseId;// 所属企业ID

	@Column(name = "parent_department_id")
	@ApiModelProperty(value = "上级部门ID")
	private Long parentDepartmentId;// 上级部门ID

	@Column(name = "is_delete")
	@ApiModelProperty(value = "是否删除")
	private Boolean isDelete;// 是否删除

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getParentDepartmentId() {
		return parentDepartmentId;
	}

	public void setParentDepartmentId(Long parentDepartmentId) {
		this.parentDepartmentId = parentDepartmentId;
	}

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
