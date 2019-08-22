package cn.emay.boot.business.system.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.emay.boot.business.system.pojo.Department;
import cn.emay.boot.business.system.pojo.User;
import cn.emay.boot.business.system.pojo.UserRoleAssign;

/**
 * 用户DTO
 * 
 * @author Frank
 *
 */
public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private Long id;
	/**
	 * 用户名
	 */
	private String username; 
	/**
	 * 真实姓名
	 */
	private String realname; 
	/**
	 * 手机号
	 */
	private String mobile; 
	/**
	 * 邮箱
	 */
	private String email; 
	/**
	 * 创建时间
	 */
	private Date createTime; 
	/**
	 * 状态[0-删除，1-停用，2-启用]
	 */
	private Integer userState; 
	/**
	 * 说明
	 */
	private String remark; 
	/**
	 * 创建者
	 */
	private Long operatorId; 
	/**
	 * 最后一次修改密码时间[用来判断登陆修改密码]
	 */
	private Date lastChangePasswordTime; 
	/**
	 * 角色名
	 */
	private String rolename; 
	/**
	 * 部门名
	 */
	private String department; 
	/**
	 * 上级部门名
	 */
	private String parentDepartment; 
	/**
	 * 部门id
	 */
	private Long departmentId; 
	/**
	 * 所有角色ID
	 */
	private List<Long> roles;

	public UserDTO() {
		this.roles = new ArrayList<Long>();
	}

	public UserDTO(User user) {
		this.createTime = user.getCreateTime();
		this.email = user.getEmail();
		this.id = user.getId();
		this.mobile = user.getMobile();
		this.realname = user.getRealname();
		this.userState = user.getUserState();
		this.username = user.getUsername();
		this.roles = new ArrayList<Long>();
	}

	public UserDTO(User user, Department department, Department parmentDepmant) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.realname = user.getRealname();
		this.mobile = user.getMobile();
		this.email = user.getEmail();
		this.createTime = user.getCreateTime();
		this.userState = user.getUserState();
		this.remark = user.getRemark();
		this.operatorId = user.getOperatorId();
		this.lastChangePasswordTime = user.getLastChangePasswordTime();
		this.department = department.getDepartmentName();
		if (parmentDepmant != null) {
			this.parentDepartment = parmentDepmant.getDepartmentName();
		} else {
			this.parentDepartment = "";
		}
		this.departmentId = department.getId();
		this.roles = new ArrayList<Long>();
	}

	public UserDTO(User user, List<UserRoleAssign> userroles) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.realname = user.getRealname();
		this.mobile = user.getMobile();
		this.email = user.getEmail();
		this.createTime = user.getCreateTime();
		this.userState = user.getUserState();
		this.remark = user.getRemark();
		this.operatorId = user.getOperatorId();
		this.lastChangePasswordTime = user.getLastChangePasswordTime();
		this.roles = new ArrayList<Long>();
		for (UserRoleAssign ur : userroles) {
			this.roles.add(ur.getRoleId());
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getUserState() {
		return userState;
	}

	public void setUserState(Integer userState) {
		this.userState = userState;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public Date getLastChangePasswordTime() {
		return lastChangePasswordTime;
	}

	public void setLastChangePasswordTime(Date lastChangePasswordTime) {
		this.lastChangePasswordTime = lastChangePasswordTime;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getParentDepartment() {
		return parentDepartment;
	}

	public void setParentDepartment(String parentDepartment) {
		this.parentDepartment = parentDepartment;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public List<Long> getRoles() {
		return roles;
	}

	public void setRoles(List<Long> roles) {
		this.roles = roles;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}
