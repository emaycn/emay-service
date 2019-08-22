package cn.emay.boot.business.system.dto;

import cn.emay.boot.business.system.pojo.Department;

/**
 * 部门DTO
 * 
 * @author lijunjian
 *
 */
public class DepartmentDTO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private Long id;
	/**
	 * 部门名称
	 */
	private String departmentName;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 父级部门名称
	 */
	private String parentDepartmentName;
	/**
	 * 父级部门ID
	 */
	private Long getParentDepartmentId;

	public DepartmentDTO() {

	}

	public DepartmentDTO(Department department, String parentName) {
		this.id = department.getId();
		this.departmentName = department.getDepartmentName();
		this.remark = department.getRemark();
		this.getParentDepartmentId = department.getParentDepartmentId();
		this.parentDepartmentName = parentName;
	}

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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getParentDepartmentName() {
		return parentDepartmentName;
	}

	public void setParentDepartmentName(String parentDepartmentName) {
		this.parentDepartmentName = parentDepartmentName;
	}

	public Long getGetParentDepartmentId() {
		return getParentDepartmentId;
	}

	public void setGetParentDepartmentId(Long getParentDepartmentId) {
		this.getParentDepartmentId = getParentDepartmentId;
	}

	@Override
	public String toString() {
		return super.toString();
	}

}