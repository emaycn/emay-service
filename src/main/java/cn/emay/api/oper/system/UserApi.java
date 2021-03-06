package cn.emay.api.oper.system;

import cn.emay.constant.web.ResourceEnum;
import cn.emay.constant.web.SystemType;
import cn.emay.constant.web.WebAuth;
import cn.emay.core.system.dto.UserInfoDTO;
import cn.emay.core.system.dto.UserItemDTO;
import cn.emay.core.system.pojo.Department;
import cn.emay.core.system.pojo.User;
import cn.emay.core.system.pojo.UserRoleAssign;
import cn.emay.core.system.service.DepartmentService;
import cn.emay.core.system.service.UserOperLogService;
import cn.emay.core.system.service.UserRoleAssignService;
import cn.emay.core.system.service.UserService;
import cn.emay.utils.CheckUtils;
import cn.emay.utils.PasswordUtils;
import cn.emay.utils.RandomUtils;
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

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户API
 *
 * @author frank
 */
@RestController
@RequestMapping(value = "/o/user", method = RequestMethod.POST)
@Api(tags = {"用户管理"})
public class UserApi {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private UserService userService;
    @Resource
    private DepartmentService departmentService;
    @Resource
    private UserRoleAssignService userRoleAssignService;
    @Resource
    private UserOperLogService userOperLogService;

    /**
     * 用户列表
     */
    @WebAuth({ResourceEnum.USER_VIEW})
    @RequestMapping("/page")
    @ApiOperation("分页查询用户列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "start", value = "起始数据位置", required = true, dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "数据条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "string"), @ApiImplicitParam(name = "realname", value = "姓名", dataType = "string"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "string"), @ApiImplicitParam(name = "userState", value = "用戶状态", dataType = "int")})
    public SuperResult<Page<UserItemDTO>> list(int start, int limit, String username, String realname, String mobile, Integer userState) {
        Page<UserItemDTO> userpage = userService.findPage(start, limit, username, realname, mobile, userState, SystemType.OPER.getType());
        log.info("user : " + WebUtils.getCurrentUser().getUsername() + " select user page.");
        return SuperResult.rightResult(userpage);
    }

    /**
     * 部门员工列表
     */
    @WebAuth({ResourceEnum.DEPARTMENT_VIEW})
    @RequestMapping("/childlist")
    @ApiImplicitParams({@ApiImplicitParam(name = "start", value = "起始数据位置", required = true, dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "数据条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "deptId", value = "部门ID", dataType = "Long"),
            @ApiImplicitParam(name = "variableName", value = "用户名/姓名/手机号", dataType = "string"),})
    @ApiOperation("部门员工列表")
    public SuperResult<Page<UserItemDTO>> list(int start, int limit, Long deptId, String variableName) {
        if (null == deptId) {
            return SuperResult.badResult("请选择部门");
        }
        Department dept = departmentService.findDepartmentById(deptId);
        if (dept == null) {
            return SuperResult.badResult("部门不存在");
        }
        Page<UserItemDTO> userList = userService.findBycondition(variableName, deptId, start, limit);
        return SuperResult.rightResult(userList);
    }

    /**
     * 停用用户
     */
    @WebAuth({ResourceEnum.USER_OPER})
    @RequestMapping("/off")
    @ApiOperation("停用用户")
    public Result off(@ApiParam(name = "userId", value = "用户ID", required = true) @RequestParam Long userId) {
        if (userId == null || userId == 0L) {
            return Result.badResult("用户不存在");
        }
        if (userId == 1L) {
            return Result.badResult("不能操作ADMIN");
        }
        User user = userService.findById(userId);
        if (user == null) {
            return Result.badResult("用户不存在");
        }
        userService.off(userId);
        String context = "停用用户:{0}";
        String module = "用户管理";
        userOperLogService.saveOperLog(module, MessageFormat.format(context, user.getUsername()));
        return Result.rightResult();
    }

    /**
     * 启用用户
     */
    @WebAuth({ResourceEnum.USER_OPER})
    @RequestMapping("/on")
    @ApiOperation("启用用户")
    public Result on(@ApiParam(name = "userId", value = "用户ID", required = true) @RequestParam Long userId) {
        if (userId == null || userId == 0L) {
            return Result.badResult("用户不存在");
        }
        if (userId == 1L) {
            return Result.badResult("不能操作ADMIN");
        }
        User user = userService.findById(userId);
        if (user == null) {
            return Result.badResult("用户不存在");
        }
        userService.on(userId);
        String context = "启用用户:{0}";
        String module = "用户管理";
        userOperLogService.saveOperLog(module, MessageFormat.format(context, user.getUsername()));
        return Result.rightResult();
    }

    /**
     * 重置密码
     */
    @WebAuth({ResourceEnum.USER_OPER})
    @RequestMapping(value = "/ajax/reset")
    public SuperResult<String> resetPassword(@ApiParam(name = "userId", value = "用户ID", required = true) @RequestParam Long userId) {
        if (userId == null || userId == 0L) {
            return SuperResult.badResult("用户不存在");
        }
        if (userId == 1L) {
            return SuperResult.badResult("不能操作ADMIN");
        }
        User user = userService.findById(userId);
        if (user == null) {
            return SuperResult.badResult("用户不存在");
        }
        User currentUser = WebUtils.getCurrentUser();
        if (currentUser.getId().equals(userId)) {
            return SuperResult.badResult("不能重置自己");
        }
        String randomPwd = RandomUtils.randomCharset(6);
        String newEnPassword = PasswordUtils.encrypt(randomPwd);
        Result result = userService.resetPassword(userId, newEnPassword);
        if (!result.getSuccess()) {
            return SuperResult.badResult(result.getMessage());
        }
        String context = "重置密码的用户:{0}";
        String module = "用户管理";
        userOperLogService.saveOperLog(module, MessageFormat.format(context, user.getUsername()));
        return SuperResult.rightResult(randomPwd);
    }

    /**
     * 删除用户
     */
    @WebAuth({ResourceEnum.USER_DELETE})
    @RequestMapping("/delete")
    @ApiOperation("删除用户")
    public Result delete(@ApiParam(name = "userId", value = "用户ID", required = true) @RequestParam Long userId) {
        if (userId == null || userId == 0L) {
            return Result.badResult("用户不存在");
        }
        User user = userService.findById(userId);
        if (user == null) {
            return Result.badResult("用户不存在");
        }
        if (userId == 1L) {
            return Result.badResult("不能操作ADMIN");
        }
        User currentUser = WebUtils.getCurrentUser();
        if (currentUser.getId().equals(userId)) {
            return Result.badResult("不能删除自己");
        }
        userService.delete(userId, SystemType.OPER.getType());
        String context = "删除用户:{0}";
        String module = "用户管理";
        userOperLogService.saveOperLog(module, MessageFormat.format(context, user.getUsername()));
        return Result.rightResult();
    }

    /**
     * 用户详细信息
     */
    @WebAuth({ResourceEnum.USER_VIEW, ResourceEnum.USER_MODIFY})
    @RequestMapping("/info")
    @ApiOperation("用户详情")
    public SuperResult<Map<String, Object>> userinfo(@ApiParam(name = "id", value = "用户ID", required = true) @RequestParam Long id) {
        if (id == null) {
            return SuperResult.badResult("用户不存在");
        }
        User user = userService.findById(id);
        if (user == null) {
            return SuperResult.badResult("用户不存在");
        }
        List<Long> roles = new ArrayList<>();
        List<UserRoleAssign> userRoles = userRoleAssignService.findByUserId(id);
        userRoles.forEach(ura -> roles.add(ura.getRoleId()));
        Department department = departmentService.findByUserId(id);
        UserInfoDTO dto = new UserInfoDTO(user);
        Map<String, Object> map = new HashMap<>();
        map.put("department", department);
        map.put("user", dto);
        map.put("roleList", roles);
        return SuperResult.rightResult(map);
    }

    /**
     * 添加用户
     */
    @WebAuth({ResourceEnum.USER_ADD})
    @RequestMapping("/add")
    @ApiOperation("添加用户")
    @ApiImplicitParams({@ApiImplicitParam(name = "username", value = "用户名", dataType = "string"),
            @ApiImplicitParam(name = "realname", value = "姓名", dataType = "string"),
            @ApiImplicitParam(name = "departmentId", value = "所属部门ID", dataType = "Long"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "string"), @ApiImplicitParam(name = "email", value = "邮箱", dataType = "string"),
            @ApiImplicitParam(name = "roleIds", value = "角色ID", dataType = "string"),})
    public SuperResult<String> add(String username, String realname, Long departmentId, String mobile, String email, String roleIds) {
        if (StringUtils.isEmpty(username)) {
            return SuperResult.badResult("用户名不能为空");
        }
        if (username.length() < 4 || username.length() > 16) {
            return SuperResult.badResult("用户名长度为4-16个字符");
        }
        if (CheckUtils.existSpecial(username)) {
            return SuperResult.badResult("用户名不能包含特殊字符");
        }
        String errorMsg = checkUserRequired(realname, mobile, email, roleIds, departmentId);
        if (!StringUtils.isEmpty(errorMsg)) {
            return SuperResult.badResult(errorMsg);
        }
        username = username.toLowerCase();
        String randomPwd = RandomUtils.randomCharset(6);
        String password = PasswordUtils.encrypt(randomPwd);
        User currentUser = WebUtils.getCurrentUser();
        userService.add(username, realname, password, email, mobile, roleIds, departmentId, null, currentUser, SystemType.OPER.getType());
        String context = "添加用户{0}";
        String module = "用户管理";
        userOperLogService.saveOperLog(module, MessageFormat.format(context, username));
        return SuperResult.rightResult(randomPwd);
    }

    /**
     * 修改用户
     */
    @WebAuth({ResourceEnum.USER_MODIFY})
    @RequestMapping("/modify")
    @ApiOperation("修改用户")
    @ApiImplicitParams({@ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long"),
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "string"), @ApiImplicitParam(name = "realname", value = "姓名", dataType = "string"),
            @ApiImplicitParam(name = "departmentId", value = "所属部门ID", dataType = "Long"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "string"), @ApiImplicitParam(name = "email", value = "邮箱", dataType = "string"),
            @ApiImplicitParam(name = "roleIds", value = "角色ID", dataType = "string"),})
    public Result modify(Long userId, String realname, Long departmentId, String mobile, String email, String roleIds) {
        if (userId <= 0L) {
            return Result.badResult("用户不存在");
        }
        User user = userService.findById(userId);
        if (user == null) {
            return Result.badResult("用户不存在");
        }
        if (userId == 1L) {
            return Result.badResult("不能操作ADMIN");
        }
        User currentUser = WebUtils.getCurrentUser();
        if (currentUser.getId().equals(userId)) {
            return Result.badResult("不能修改自己");
        }
        String errorMsg = checkUserRequired(realname, mobile, email, roleIds, departmentId);
        if (!StringUtils.isEmpty(errorMsg)) {
            return Result.badResult(errorMsg);
        }
        Result result = userService.modify(realname, email, mobile, roleIds, userId, departmentId, SystemType.OPER.getType());
        if (!result.getSuccess()) {
            return result;
        }
        String context = "修改用户{0}";
        String module = "用户管理";
        userOperLogService.saveOperLog(module, MessageFormat.format(context, user.getUsername()));
        return Result.rightResult();
    }

    /**
     * 校验用户信息
     *
     * @param realname     姓名
     * @param mobile       手机号
     * @param email        邮箱
     * @param roleIds      角色ID集合
     * @param departmentId 部门ID
     * @return 错误信息
     */
    private String checkUserRequired(String realname, String mobile, String email, String roleIds, Long departmentId) {
        if (StringUtils.isEmpty(realname)) {
            return "姓名不能为空";
        }
        if (realname.length() > 10) {
            return "姓名不能超过10个字符";
        }
        if (CheckUtils.existSpecial(realname)) {
            return "姓名不能包含特殊字符";
        }
        if (!CheckUtils.isChineseOrEnglish(realname)) {
            return "姓名只能包含中英文";
        }
        if (StringUtils.isEmpty(email)) {
            return "邮箱不能为空";
        }
        if (!CheckUtils.isEmail(email)) {
            return "请输入正确的邮箱";
        }
        if (!CheckUtils.isMobile(mobile)) {
            return "手机号码格式不正确";
        }
        if (StringUtils.isEmpty(roleIds)) {
            return "角色不能为空";
        }
        if (null == departmentId) {
            return "请选择部门";
        }
        return null;
    }

}
