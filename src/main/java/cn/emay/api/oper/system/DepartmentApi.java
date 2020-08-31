package cn.emay.api.oper.system;

import cn.emay.constant.web.OperType;
import cn.emay.constant.web.ResourceEnum;
import cn.emay.constant.web.WebAuth;
import cn.emay.core.system.dto.DepartmentDTO;
import cn.emay.core.system.pojo.Department;
import cn.emay.core.system.service.DepartmentService;
import cn.emay.core.system.service.UserDepartmentAssignService;
import cn.emay.core.system.service.UserOperLogService;
import cn.emay.utils.CheckUtils;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.Result;
import cn.emay.utils.result.SuperResult;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.List;

/**
 * 部门API
 *
 * @author lijunjian
 */
@RestController
@Api(tags = {"部门管理"})
@RequestMapping(value = "/o/department", method = RequestMethod.POST)
public class DepartmentApi {
    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private UserDepartmentAssignService userDepartmentAssignService;
    @Autowired
    private UserOperLogService userOperLogService;

    /**
     * 部门树形
     */
    @WebAuth({ResourceEnum.DEPARTMENT_VIEW, ResourceEnum.USER_ADD, ResourceEnum.USER_MODIFY})
    @RequestMapping("/getTree")
    @ApiOperation("部门树形")
    public SuperResult<List<Department>> getTree() {
        List<Department> list = departmentService.findByParentId(0L);
        return SuperResult.rightResult(list);
    }

    /**
     * 展开子节点
     */
    @WebAuth({ResourceEnum.DEPARTMENT_VIEW, ResourceEnum.USER_ADD, ResourceEnum.USER_MODIFY})
    @RequestMapping("/showChildrenNode")
    @ApiOperation("树子节点")
    public SuperResult<List<Department>> showChildrenNode(
            @ApiParam(name = "id", value = "部门ID", required = true) @RequestParam Long id) {
        if (null == id) {
            return SuperResult.badResult("请选择部门");
        }
        List<Department> childrenNode = departmentService.findByParentId(id);
        return SuperResult.rightResult(childrenNode);
    }

    /**
     * 部门列表
     */
    @WebAuth({ResourceEnum.DEPARTMENT_VIEW})
    @RequestMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "start", value = "起始数据位置", required = true, dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "数据条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "id", value = "父级部门ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "departmentName", value = "部门名称", dataType = "string"),

    })
    @ApiOperation("部门列表")
    public SuperResult<Page<DepartmentDTO>> findDepartment(int start, int limit, Long id, String departmentName) {
        if (null == id) {
            return SuperResult.badResult("请选择部门");
        }
        Page<DepartmentDTO> page = departmentService.findPage(id, departmentName, start, limit);
        return SuperResult.rightResult(page);
    }

    /**
     * 部门新增
     */
    @WebAuth({ResourceEnum.DEPARTMENT_ADD})
    @RequestMapping("/add")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "departmentName", value = "部门名称", required = true, dataType = "string"),
            @ApiImplicitParam(name = "parentId", value = "上级部门ID", required = true, dataType = "Long"),

    })
    @ApiOperation("部门新增")
    public Result add(String departmentName, Long parentId) {
        if (null == parentId) {
            return Result.badResult("上级部门不存在");
        }
        String errorMsg = checkInfo(departmentName, parentId, null);
        if (!StringUtils.isEmpty(errorMsg)) {
            return Result.badResult(errorMsg);
        }
        Department department = new Department();
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
        userOperLogService.saveOperLog(module, MessageFormat.format(context, new Object[]{departmentName}),
                OperType.ADD);
        return Result.rightResult();
    }

    /**
     * 删除部门
     */
    @WebAuth({ResourceEnum.DEPARTMENT_DELETE})
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
            return Result.badResult("该部门下有子部门，不能删除");
        }
        Long userCount = userDepartmentAssignService.findByDepId(id);
        if (userCount > 0) {
            return Result.badResult("该部门下有用户，不能删除");
        }
        departmentService.deleteDepartment(id);
        String context = "删除部门:{0}";
        String module = "部门管理";
        userOperLogService.saveOperLog(module,
                MessageFormat.format(context, new Object[]{department.getDepartmentName()}), OperType.DELETE);
        return Result.rightResult();
    }

    /**
     * 修改部门名称
     */
    @WebAuth({ResourceEnum.DEPARTMENT_MODIFY})
    @RequestMapping("/modify")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "部门ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "departmentName", value = "部门名称", required = true, dataType = "string"),})
    @ApiOperation("修改部门名称")
    public Result modify(Long id, String departmentName) {
        if (null == id) {
            return Result.badResult("部门ID不能为空");
        }
        Department dept = departmentService.findDepartmentById(id);
        if (dept == null) {
            return Result.badResult("该部门不存在");
        }
        String errorMsg = checkInfo(departmentName, dept.getParentDepartmentId(), id);
        if (!StringUtils.isEmpty(errorMsg)) {
            return Result.badResult(errorMsg);
        }
        dept.setDepartmentName(departmentName);
        departmentService.modifyDepartment(dept);
        String context = "修改部门:{0}";
        String module = "部门管理";
        userOperLogService.saveOperLog(module, MessageFormat.format(context, new Object[]{departmentName}),
                OperType.MODIFY);
        return Result.rightResult();
    }

    /**
     * 校验信息
     *
     * @param name     部门名称
     * @param parentId 上级部门ID
     * @param ignoreId 忽略对比的部门ID
     * @return
     */
    public String checkInfo(String name, Long parentId, Long ignoreId) {
        if (StringUtils.isEmpty(name)) {
            return "部门名称不能为空";
        }
        if (name.length() > 20) {
            return "部门名称长度不能超过20个字符";
        }
        if (!CheckUtils.isChineseOrEnglish(name)) {
            return "请输入中文和英文";
        }
        if (CheckUtils.existSpecial(name)) {
            return "名称不能包含特殊字符";
        }
        Boolean has = departmentService.hasSameDepartmentNameAtParent(name, parentId, ignoreId);
        if (has) {
            return "同一级已有相同的部门名称";
        }
        return null;
    }
}
