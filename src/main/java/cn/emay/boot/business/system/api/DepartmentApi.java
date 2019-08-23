package cn.emay.boot.business.system.api;

import java.text.MessageFormat;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.emay.boot.base.constant.ResourceEnum;
import cn.emay.boot.base.web.WebAuth;
import cn.emay.boot.business.system.dto.DepartmentDTO;
import cn.emay.boot.business.system.dto.UserDTO;
import cn.emay.boot.business.system.pojo.Department;
import cn.emay.boot.business.system.pojo.UserOperLog;
import cn.emay.boot.business.system.service.DepartmentService;
import cn.emay.boot.business.system.service.UserDepartmentAssignService;
import cn.emay.boot.business.system.service.UserOperLogService;
import cn.emay.boot.business.system.service.UserService;
import cn.emay.boot.utils.CheckUtils;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.Result;
import cn.emay.utils.result.SuperResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 部门API
 * 
 * @author lijunjian
 *
 */
@RestController
@Api(tags = { "部门管理" })
@RequestMapping(value = "/department", method = RequestMethod.POST)
public class DepartmentApi {
	Logger log = LoggerFactory.getLogger(getClass());

	@Resource
	private DepartmentService departmentService;
	@Resource
	private UserDepartmentAssignService userDepartmentAssignService;
	@Resource
	private UserService userService;
	@Autowired
	private UserOperLogService userOperLogService;

	/**
	 * 部门列表
	 *
	 */
	@WebAuth({ ResourceEnum.DEPARTMENT_VIEW })
	@RequestMapping("/list")
	@ApiImplicitParams({ @ApiImplicitParam(name = "start", value = "起始数据位置", required = true, dataType = "int"), @ApiImplicitParam(name = "limit", value = "数据条数", required = true, dataType = "int"),
			@ApiImplicitParam(name = "id", value = "父级部门ID", required = true, dataType = "Long"), @ApiImplicitParam(name = "departmentName", value = "部门名称", dataType = "string"),

	})
	@ApiOperation("部门列表")
	public SuperResult<Page<DepartmentDTO>> findDepartment(int start, int limit, Long id, String departmentName) {
		if (null == id) {
			return SuperResult.badResult("请选择部门");
		}
		Page<DepartmentDTO> page = departmentService.findDepartmentByLikeName(id, departmentName, start, limit);
		return SuperResult.rightResult(page);
	}

	/**
	 * 部门新增
	 *
	 */
	@WebAuth({ ResourceEnum.DEPARTMENT_ADD })
	@RequestMapping("/add")
	@ApiImplicitParams({ @ApiImplicitParam(name = "departmentName", value = "部门名称", required = true, dataType = "string"),
			@ApiImplicitParam(name = "parentId", value = "上级部门ID", required = true, dataType = "Long"),

	})
	@ApiOperation("部门新增")
	public Result add(String departmentName, Long parentId) {
		Department department = new Department();
		String errorMsg = checkInfo(departmentName, null);
		if (!StringUtils.isEmpty(errorMsg)) {
			return Result.badResult(errorMsg);
		}
		if (null == parentId) {
			return Result.badResult("上级部门不存在");
		}
		if (parentId == 0L) {
			department.setFullPath(0 + ",");
		} else {
			Department dept = departmentService.findDepartmentById(parentId);
			if (dept == null) {
				return Result.badResult("上级部门不存在");
			}
			department.setFullPath(dept.getFullPath() + parentId + ",");
		}
		department.setParentDepartmentId(parentId);
		department.setDepartmentName(departmentName);
		departmentService.addDepartment(department);
		String context = "添加部门:{0}";
		String module = "部门管理";
		userOperLogService.log(module, MessageFormat.format(context, new Object[] { departmentName }), UserOperLog.OPERATE_ADD);
		return Result.rightResult();
	}

	/**
	 * 删除部门
	 */
	@WebAuth({ ResourceEnum.DEPARTMENT_DELETE })
	@RequestMapping("/delete")
	@ApiOperation("删除部门")
	public Result delete(@ApiParam(name = "id", value = "部门ID", required = true) @RequestParam Long id) {
		if (null == id) {
			return Result.badResult("部门ID不能为空");
		}
		Department department = departmentService.findDepartmentById(id);
		if (null == department) {
			return Result.badResult("部门不存在");
		}
		Long depCount = departmentService.findCountByParentId(id);
		if (depCount > 0) {
			return Result.badResult("该部门下有子部门，请先删除子部门");
		}
		Long userCount = userDepartmentAssignService.findByDepId(id);
		if (userCount > 0) {
			return Result.badResult("该部门下有用户存在，不能删除");
		}
		departmentService.deleteDepartment(id);
		String context = "删除部门:{0}";
		String module = "部门管理";
		userOperLogService.log(module, MessageFormat.format(context, new Object[] { department.getDepartmentName() }), UserOperLog.OPERATE_DELETE);
		return Result.rightResult();
	}

	/**
	 * 部门详情
	 */
	@WebAuth({ ResourceEnum.DEPARTMENT_VIEW })
	@RequestMapping("/detail")
	@ApiOperation("部门详情")
	public SuperResult<DepartmentDTO> detail(@ApiParam(name = "deptId", value = "部门ID", required = true) @RequestParam Long deptId) {
		if (null == deptId) {
			return SuperResult.badResult("请选择部门");
		}
		Department department = departmentService.findDepartmentById(deptId);
		if (department == null) {
			return SuperResult.badResult("部门不存在");
		}
		Department parentDepartment = null;
		if (department.getParentDepartmentId() != 0L) {
			parentDepartment = departmentService.findDepartmentById(department.getParentDepartmentId());
			if (parentDepartment == null) {
				return SuperResult.badResult("该部门信息有误");
			}
		}
		String departmentName = "";
		if (department.getParentDepartmentId() != 0L) {
			departmentName = parentDepartment.getDepartmentName();
		}
		DepartmentDTO dto = new DepartmentDTO(department, departmentName);
		return SuperResult.rightResult(dto);
	}

	/**
	 * 修改部门名称
	 */
	@WebAuth({ ResourceEnum.DEPARTMENT_MODIFY })
	@RequestMapping("/modify")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "部门ID", required = true, dataType = "Long"),
			@ApiImplicitParam(name = "departmentName", value = "部门名称", required = true, dataType = "string"), })
	@ApiOperation("修改部门名称")
	public Result modify(Long id, String departmentName) {
		String errorMsg = "";
		if (null == id) {
			return Result.badResult("部门ID不能为空");
		}
		Department dept = departmentService.findDepartmentById(id);
		if (dept == null) {
			return Result.badResult("该部门不存在");
		}
		errorMsg = checkInfo(departmentName, id);
		if (!StringUtils.isEmpty(errorMsg)) {
			return Result.badResult(errorMsg);
		}
		dept.setDepartmentName(departmentName);
		departmentService.modifyDepartment(dept);
		String context = "修改部门:{0}";
		String module = "部门管理";
		userOperLogService.log(module,  MessageFormat.format(context, new Object[] { departmentName }), UserOperLog.OPERATE_MODIFY);
		return Result.rightResult();
	}

	/**
	 * 部门树形
	 */
	@WebAuth({ ResourceEnum.DEPARTMENT_VIEW })
	@RequestMapping("/getTree")
	@ApiOperation("部门树形")
	public SuperResult<List<Department>> getTree() {
		List<Department> list = departmentService.findByParentId(0L);
		return SuperResult.rightResult(list);
	}

	/**
	 * 展开子节点
	 */
	@WebAuth({ ResourceEnum.DEPARTMENT_VIEW })
	@RequestMapping("/showChildrenNode")
	@ApiOperation("树子节点")
	public SuperResult<List<Department>> showChildrenNode(@ApiParam(name = "id", value = "部门ID", required = true) @RequestParam Long id) {
		if (null == id) {
			return SuperResult.badResult("请选择部门");
		}
		List<Department> childrenNode = departmentService.findByParentId(id);
		return SuperResult.rightResult(childrenNode);
	}

	/**
	 * 部门员工列表
	 */
	@WebAuth({ ResourceEnum.DEPARTMENT_VIEW })
	@RequestMapping("/childlist")
	@ApiImplicitParams({ @ApiImplicitParam(name = "start", value = "起始数据位置", required = true, dataType = "int"), @ApiImplicitParam(name = "limit", value = "数据条数", required = true, dataType = "int"),
			@ApiImplicitParam(name = "deptId", value = "部门ID", dataType = "Long"), @ApiImplicitParam(name = "variableName", value = "用户名/姓名/手机号", dataType = "string"), })
	@ApiOperation("部门员工列表")
	public SuperResult<Page<UserDTO>> list(int start, int limit, Long deptId, String variableName) {
		if (null == deptId) {
			return SuperResult.badResult("请选择部门");
		}
		Department dept = departmentService.findDepartmentById(deptId);
		if (dept == null) {
			return SuperResult.badResult("部门不存在");
		}
		Page<UserDTO> userList = userService.findBycondition(variableName, deptId, start, limit);
		return SuperResult.rightResult(userList);
	}

	public Result check(Long depId) {
		if (depId > 0L) {
			Department department = departmentService.findDepartmentById(depId);
			if (null == department) {
				return Result.badResult("部门不存在");
			}
		}
		return Result.rightResult();
	}

	/**
	 * 校验信息
	 */
	public String checkInfo(String name, Long id) {
		String errorMsg = "";
		if (StringUtils.isEmpty(name)) {
			errorMsg = "部门名称不能为空";
			return errorMsg;
		}
		if (name.length() > 20) {
			errorMsg = "部门名称长度不能超过20个字符";
			return errorMsg;
		}
		if (!CheckUtils.isChineseOrEnglish(name)) {
			errorMsg = "请输入中文和英文";
			return errorMsg;
		}
		if (CheckUtils.existSpecial(name)) {
			errorMsg = "名称不能包含特殊字符";
			return errorMsg;
		}
		Long count = departmentService.findDepartmentByName(name, id);
		if (count > 0L) {
			errorMsg = "部门名称已存在";
			return errorMsg;
		}
		return errorMsg;
	}
}
