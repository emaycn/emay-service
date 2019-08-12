package cn.emay.boot.business.system.dto;

import cn.emay.boot.business.system.pojo.Department;

/**
 * 
* @项目名称：ebdp-web-operation 
* @类描述： 部门dto  
* @创建人：lijunjian   
* @创建时间：2019年7月8日 上午10:40:39   
* @修改人：lijunjian   
* @修改时间：2019年7月8日 上午10:40:39   
* @修改备注：
 */
public class DepartmentDTO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String departmentCode;// 部门编码
	private String departmentName;// 部门名称
	private Long enterpriseId;// 所属企业ID
	private String remark;// 备注
	private Boolean isDelete;// 是否删除
	private String parentDepartmentName;// 父级部门名称
	private String state;// 状态,1部门，2人员
	private Long getParentDepartmentId;

	public DepartmentDTO() {

	}

	public DepartmentDTO(Department department, String parentName) {
		this.id = department.getId();
		this.departmentCode = department.getDepartmentCode();
		this.departmentName = department.getDepartmentName();
		this.enterpriseId = department.getEnterpriseId();
		this.remark = department.getRemark();
		this.isDelete = department.getIsDelete();
		this.getParentDepartmentId=department.getParentDepartmentId();
		this.parentDepartmentName = parentName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
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

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	public String getParentDepartmentName() {
		return parentDepartmentName;
	}

	public void setParentDepartmentName(String parentDepartmentName) {
		this.parentDepartmentName = parentDepartmentName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getGetParentDepartmentId() {
		return getParentDepartmentId;
	}

	public void setGetParentDepartmentId(Long getParentDepartmentId) {
		this.getParentDepartmentId = getParentDepartmentId;
	}
	
}