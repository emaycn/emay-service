package cn.emay.api.oper.client;

import cn.emay.constant.web.ResourceEnum;
import cn.emay.constant.web.SystemType;
import cn.emay.constant.web.WebAuth;
import cn.emay.core.system.pojo.Resource;
import cn.emay.core.system.pojo.Role;
import cn.emay.core.system.pojo.RoleResourceAssign;
import cn.emay.core.system.service.ResourceService;
import cn.emay.core.system.service.RoleService;
import cn.emay.core.system.service.UserOperLogService;
import cn.emay.core.system.service.UserRoleAssignService;
import cn.emay.utils.CheckUtils;
import cn.emay.utils.WebUtils;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.Result;
import cn.emay.utils.result.SuperResult;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色API
 *
 * @author frank
 */
@RequestMapping(value = "/o/clientrole", method = RequestMethod.POST)
@RestController
@Api(tags = {"角色管理"})
public class ClientRoleApi {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @javax.annotation.Resource
    private RoleService roleService;
    @javax.annotation.Resource
    private ResourceService resourceService;
    @javax.annotation.Resource
    private UserRoleAssignService userRoleAssignService;
    @javax.annotation.Resource
    private UserOperLogService userOperLogService;

    /**
     * 角色列表
     */
    @WebAuth({ResourceEnum.CLIENTROLE_VIEW})
    @RequestMapping("/page")
    @ApiOperation("分页查询角色列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "start", value = "起始数据位置", required = true, dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "数据条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "roleName", value = "角色名称", dataType = "string"),})
    public SuperResult<Page<Role>> rolelist(int start, int limit, String roleName) {
        Page<Role> userpage = roleService.findPage(start, limit, roleName, SystemType.CLIENT.getType());
        return SuperResult.rightResult(userpage);
    }

    /**
     * 添加角色
     */
    @WebAuth({ResourceEnum.CLIENTROLE_ADD})
    @RequestMapping("/add")
    @ApiOperation("添加角色")
    @ApiImplicitParams({@ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "string"),
            @ApiImplicitParam(name = "remark", value = "角色描述", required = true, dataType = "string"),
            @ApiImplicitParam(name = "resourceCodes", value = "角色编码权限(逗号分割)", required = true, dataType = "string")})
    public Result add(String roleName, String remark, String resourceCodes, Long clientId) {
        List<RoleResourceAssign> roleList = new ArrayList<>();
        String errorMsg = checkData(roleName, resourceCodes, remark, null, roleList, SystemType.CLIENT.getType());
        if (!StringUtils.isEmpty(errorMsg)) {
            return Result.badResult(errorMsg);
        }
        roleService.add(roleName, remark, roleList, SystemType.CLIENT.getType());
        String context = "添加角色:{0}";
        String module = "角色管理";
        userOperLogService.saveOperLog(module, MessageFormat.format(context, roleName));
        return Result.rightResult();
    }

    /**
     * 修改角色权限
     */
    @ApiOperation("修改角色权限")
    @WebAuth({ResourceEnum.CLIENTROLE_MODIFY})
    @RequestMapping("/modify")
    @ApiImplicitParams({@ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "string"),
            @ApiImplicitParam(name = "remark", value = "角色描述", required = true, dataType = "string"),
            @ApiImplicitParam(name = "clientId", value = "客戶ID", dataType = "Long"),
            @ApiImplicitParam(name = "resourceCodes", value = "角色编码权限(逗号分割)", required = true, dataType = "string"),})
    public Result modify(Long roleId, String roleName, String remark, String resourceCodes, Long clientId) {
        if (roleId == 1L) {
            return Result.badResult("管理员角色不能修改");
        }
        List<RoleResourceAssign> roleList = new ArrayList<>();
        String errorMsg = checkData(roleName, resourceCodes, remark, roleId, roleList, SystemType.CLIENT.getType());
        if (!StringUtils.isEmpty(errorMsg)) {
            return Result.badResult(errorMsg);
        }
        roleService.modify(roleId, roleName, remark, roleList);
        String context = "修改角色:{0}";
        String module = "角色管理";
        userOperLogService.saveOperLog(module, MessageFormat.format(context, roleName));
        return Result.rightResult();
    }

    /**
     * 系统所有角色
     */
    @WebAuth({ResourceEnum.USER_ADD, ResourceEnum.USER_MODIFY})
    @RequestMapping("/allRole")
    @ApiOperation("系统所有角色")
    public SuperResult<List<Role>> allRoles() {
        List<Role> roles = roleService.findAll();
        log.info("user : " + WebUtils.getCurrentUser().getUsername() + " select all roles.");
        return SuperResult.rightResult(roles);
    }

    /**
     * 角色所有资源
     */
    @WebAuth({ResourceEnum.CLIENTROLE_VIEW})
    @RequestMapping(value = "/roleResource")
    @ApiOperation("角色所有资源")
    public SuperResult<List<Resource>> roleResource(
            @ApiParam(name = "roleId", value = "角色ID", required = true) @RequestParam Long roleId) {
        List<Resource> userResource = resourceService.getRoleResources(roleId);
        return SuperResult.rightResult(userResource);
    }

    /**
     * 删除角色
     */
    @WebAuth({ResourceEnum.CLIENTROLE_DELETE})
    @RequestMapping("/delete")
    @ApiOperation("删除角色")
    @ApiImplicitParams({@ApiImplicitParam(name = "clientId", value = "客戶ID", dataType = "Long"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long")})
    public Result delete(Long clientId, Long roleId) {
        if (roleId == null || roleId == 0L) {
            return Result.badResult("角色不存在");
        }
        if (roleId == 1L) {
            return Result.badResult("管理员角色不能删除");
        }
        Role role = roleService.findById(roleId);
        if (role == null) {
            return Result.badResult("角色不存在");
        }
        Boolean isExists = userRoleAssignService.findExistsByRoleId(roleId);
        if (isExists) {
            return Result.badResult("角色已关联用户，无法删除.");
        }
        roleService.delete(roleId);
        String context = "删除角色:{0}";
        String module = "角色管理";
        userOperLogService.saveOperLog(module, MessageFormat.format(context, role.getRoleName()));
        return Result.rightResult();
    }

    /**
     * 检测新增/修改数据
     *
     * @param roleName 角色名
     * @param auths    权限集合
     * @param remark   备注
     * @param roleId   角色ID
     * @param roleList 权限接收容器
     * @return 错误信息
     */
    private String checkData(String roleName, String auths, String remark, Long roleId,
                             List<RoleResourceAssign> roleList, String type) {
        if (StringUtils.isEmpty(roleName)) {
            return "角色名不能为空";
        }
        if (StringUtils.isEmpty(auths)) {
            return "权限不能为空";
        }
        if (roleName.length() > 20) {
            return "角色名称长度不可超过20个字符";
        }
        if (!StringUtils.isEmpty(remark) && remark.length() > 50) {
            return "角色描述不能超过50个字符";
        }
        if (CheckUtils.existSpecial(roleName)) {
            return "角色名称不合法";
        }
        // 修改时校验角色是否存在
        if (null != roleId) {
            Role role = roleService.findById(roleId);
            if (role == null) {
                return "角色不存在";
            }
        }
        // 角色名称唯一校验
        boolean hasSame = roleService.hasSameRoleName(roleName, roleId);
        if (hasSame) {
            return "角色名已存在";
        }
        // 校验权限
        List<Resource> allResource = resourceService.getByResourceType(type.toUpperCase());
        Map<String, Long> map = new HashMap<>();
        for (Resource resource : allResource) {
            map.put(resource.getResourceCode(), resource.getId());
        }
        List<RoleResourceAssign> roleList1 = new ArrayList<>();
        String[] aus = auths.split(",");
        for (String au : aus) {
            Long auId = map.get(au);
            if (auId == null) {
                continue;
            }
            RoleResourceAssign roleResourceAssign = new RoleResourceAssign(roleId, auId);
            roleList1.add(roleResourceAssign);
        }
        if (roleList1.size() == 0) {
            return "权限不能为空";
        }
        roleList.addAll(roleList1);
        return null;
    }

}
