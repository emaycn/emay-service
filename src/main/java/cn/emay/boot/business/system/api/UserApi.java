package cn.emay.boot.business.system.api;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
import cn.emay.boot.business.system.dto.UserDTO;
import cn.emay.boot.business.system.pojo.Department;
import cn.emay.boot.business.system.pojo.Role;
import cn.emay.boot.business.system.pojo.User;
import cn.emay.boot.business.system.pojo.UserDepartmentAssign;
import cn.emay.boot.business.system.pojo.UserOperLog;
import cn.emay.boot.business.system.pojo.UserRoleAssign;
import cn.emay.boot.business.system.service.DepartmentService;
import cn.emay.boot.business.system.service.RoleService;
import cn.emay.boot.business.system.service.UserDepartmentAssignService;
import cn.emay.boot.business.system.service.UserOperLogService;
import cn.emay.boot.business.system.service.UserRoleAssignService;
import cn.emay.boot.business.system.service.UserService;
import cn.emay.boot.utils.CheckUtil;
import cn.emay.boot.utils.PasswordUtils;
import cn.emay.boot.utils.RandomNumberUtils;
import cn.emay.boot.utils.WebUtils;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.encryption.Md5;
import cn.emay.utils.result.Result;
import cn.emay.utils.result.SuperResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

/**
 * 用户API
 * 
 * @author frank
 *
 */
@RestController
@RequestMapping(value = "/user", method = RequestMethod.POST)
@Api(tags = { "用户管理" })
public class UserApi {

	Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserService userService;
	@Resource
	private DepartmentService departmentService;
	@Resource
	private UserOperLogService userOperLogService;
	@Resource
	private UserRoleAssignService userRoleAssignService;
	@Resource
	private UserDepartmentAssignService userDepartmentAssignService;
	@Resource
	private RoleService roleService;

	/**
	 * 用户列表
	 */
	@WebAuth({ ResourceEnum.USER_VIEW })
	@RequestMapping("/page")
	@ApiOperation("分页查询用户列表")
	@ApiImplicitParams({ @ApiImplicitParam(name = "start", value = "起始数据位置", required = true, dataType = "int"), @ApiImplicitParam(name = "limit", value = "数据条数", required = true, dataType = "int"),
			@ApiImplicitParam(name = "username", value = "用户名", dataType = "string"), @ApiImplicitParam(name = "realname", value = "姓名", dataType = "string"),
			@ApiImplicitParam(name = "mobile", value = "手机号", dataType = "string"), })
	public SuperResult<Page<UserDTO>> list(int start, int limit, String username, String realname, String mobile) {
		Page<UserDTO> userpage = userService.findPage(start, limit, username, realname, mobile);
		return SuperResult.rightResult(userpage);
	}

	/**
	 * 用户详细信息
	 */
	@WebAuth({ ResourceEnum.USER_VIEW })
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
		List<UserRoleAssign> userRoles = userRoleAssignService.findByUserId(id);
		UserDepartmentAssign userDepartmentAssign = userDepartmentAssignService.findByUserId(id);
		UserDTO dto = new UserDTO(user, userRoles);
		Map<String, Object> map = new HashMap<String, Object>();
		if (null != userDepartmentAssign) {
			Department department = departmentService.findDepartmentById(userDepartmentAssign.getSystemDepartmentId());
			map.put("department", department);
		}
		map.put("user", dto);// 用户
		map.put("roleList", userRoles);// 角色
		return SuperResult.rightResult(map);
	}

	/**
	 * 修改用户
	 */
	@WebAuth({ ResourceEnum.USER_MODIFY })
	@RequestMapping("/modify")
	@ApiOperation("修改用户")
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long"), @ApiImplicitParam(name = "username", value = "用户名", dataType = "string"),
			@ApiImplicitParam(name = "realname", value = "姓名", dataType = "string"), @ApiImplicitParam(name = "departmentId", value = "所属部门ID", dataType = "Long"),
			@ApiImplicitParam(name = "mobile", value = "手机号", dataType = "string"), @ApiImplicitParam(name = "email", value = "邮箱", dataType = "string"),
			@ApiImplicitParam(name = "roleIds", value = "角色ID", dataType = "string"), })
	public Result modify(Long userId, String username, String realname, Long departmentId, String mobile, String email, String roleIds) {
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
		String errorMsg = checkUserRequired(username, realname, mobile, email, roleIds, departmentId, userId);
		if (!StringUtils.isEmpty(errorMsg)) {
			return Result.badResult(errorMsg);
		}
		Result result = userService.modify(username, realname, email, mobile, roleIds, userId, departmentId);
		if (!result.getSuccess()) {
			return result;
		}
		User currentUser = WebUtils.getCurrentUser();
		String context = "修改用户{0}";
		String module = "用户管理";
		userOperLogService.saveLog(module, currentUser.getId(), currentUser.getUsername(), MessageFormat.format(context, new Object[] { username }), UserOperLog.OPERATE_MODIFY);
		log.info("用户:" + currentUser.getUsername() + "修改用户,用户名称为:" + username);
		return Result.rightResult();
	}

	/**
	 * 添加用户
	 */
	@WebAuth({ ResourceEnum.USER_ADD })
	@RequestMapping("/add")
	@ApiOperation("添加用户")
	@ApiImplicitParams({ @ApiImplicitParam(name = "username", value = "用户名", dataType = "string"), @ApiImplicitParam(name = "realname", value = "姓名", dataType = "string"),
			@ApiImplicitParam(name = "departmentId", value = "所属部门ID", dataType = "Long"), @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "string"),
			@ApiImplicitParam(name = "email", value = "邮箱", dataType = "string"), @ApiImplicitParam(name = "roleIds", value = "角色ID", dataType = "string"), })
	public Result add(String username, String realname, Long departmentId, String mobile, String email, String roleIds, String remark) {
		String randomPwd = RandomNumberUtils.getNumbersAndLettersRandom(6);
		String password = PasswordUtils.encrypt(Md5.md5(randomPwd.getBytes()));
		User currentUser = WebUtils.getCurrentUser();
		Result result = userService.add(username, realname, password, email, mobile, roleIds, departmentId, currentUser);
		if (!result.getSuccess()) {
			return result;
		}
		String context = "添加用户{0}";
		String module = "用户管理";
		userOperLogService.saveLog(module, currentUser.getId(), currentUser.getUsername(), MessageFormat.format(context, new Object[] { username }), UserOperLog.OPERATE_ADD);
		log.info("用户:" + currentUser.getUsername() + "添加用户,用户名称为:" + username);
		return Result.rightResult(randomPwd);
	}

	/**
	 * 删除用户
	 */
	@WebAuth({ ResourceEnum.USER_DELETE })
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
		userService.delete(userId);
		User currentUser = WebUtils.getCurrentUser();
		String context = "删除用户:{0}";
		String module = "用户管理";
		userOperLogService.saveLog(module, currentUser.getId(), currentUser.getUsername(), MessageFormat.format(context, new Object[] { user.getUsername() }), UserOperLog.OPERATE_DELETE);
		log.info("用户:" + currentUser.getUsername() + "删除用户,用户名称为:" + user.getUsername());
		return Result.rightResult();
	}

	/**
	 * 启用用户
	 */
	@WebAuth({ ResourceEnum.USER_OPER })
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
		User currentUser = WebUtils.getCurrentUser();
		String context = "启用用户:{0}";
		String module = "用户管理";
		userOperLogService.saveLog(module, currentUser.getId(), currentUser.getUsername(), MessageFormat.format(context, new Object[] { user.getUsername() }), UserOperLog.OPERATE_DELETE);
		log.info("用户:" + currentUser.getUsername() + "启用用户,用户名称为:" + user.getUsername());
		return Result.rightResult();
	}

	/**
	 * 停用用户
	 */
	@WebAuth({ ResourceEnum.USER_OPER })
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
		User currentUser = WebUtils.getCurrentUser();
		String context = "停用用户:{0}";
		String module = "用户管理";
		userOperLogService.saveLog(module, currentUser.getId(), currentUser.getUsername(), MessageFormat.format(context, new Object[] { user.getUsername() }), UserOperLog.OPERATE_DELETE);
		log.info("用户:" + currentUser.getUsername() + "停用用户,用户名称为:" + user.getUsername());
		return Result.rightResult();
	}

	/**
	 * 重置密码
	 */
	@WebAuth({ ResourceEnum.USER_OPER })
	@RequestMapping(value = "/ajax/reset")
	public Result resetPassword(@ApiParam(name = "userId", value = "用户ID", required = true) @RequestParam Long userId) {
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
		User currentUser = WebUtils.getCurrentUser();
		if (currentUser.getId().equals(userId)) {
			return Result.badResult("不能重置自己");
		}
		Result result = userService.updateResetUserPassword(user);
		if (!result.getSuccess()) {
			return result;
		}
		String context = "重置密码的用户:{0}";
		String module = "用户管理";
		userOperLogService.saveLog(module, currentUser.getId(), currentUser.getUsername(), MessageFormat.format(context, new Object[] { user.getUsername() }), UserOperLog.OPERATE_MODIFY);
		log.info("用户:" + currentUser.getUsername() + "重置用户密码,用户名称为:" + user.getUsername());
		return Result.rightResult(result.getResult());
	}

	/**
	 * 部门树形
	 */
	@WebAuth({ ResourceEnum.USER_VIEW })
	@RequestMapping("/getTree")
	@ApiOperation("部门树形")
	public SuperResult<List<Department>> getTree() {
		List<Department> list = departmentService.findByParentId(0L);
		return SuperResult.rightResult(list);
	}

	/**
	 * 展开子节点
	 */
	@WebAuth({ ResourceEnum.USER_VIEW })
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
	 * 系统所有角色
	 */
	@WebAuth({ ResourceEnum.USER_VIEW })
	@RequestMapping("/allRole")
	@ApiOperation("系统所有角色")
	public SuperResult<List<Role>> allRoles() {
		List<Role> roles = roleService.findAll();
		return SuperResult.rightResult(roles);
	}

	/**
	 * 校验用户信息
	 */
	private String checkUserRequired(String username, String nickname, String mobile, String email, String roles, Long department, Long userId) {
		String errorMsg = "";
		if (StringUtils.isEmpty(username)) {
			errorMsg = "用户名不能为空";
			return errorMsg;
		}
		if (username.length() < 4 || username.length() > 16) {
			errorMsg = "用户名长度为4-16个字符";
			return errorMsg;
		}
		if (!CheckUtil.notExistSpecial(username)) {
			errorMsg = "用户名不能包含特殊字符";
			return errorMsg;
		}
		username = username.toLowerCase();
		if (!CheckUtil.checkUserName(username)) {
			errorMsg = "请输入正确的用户名";
			return errorMsg;
		}
		if (StringUtils.isEmpty(nickname)) {
			errorMsg = "姓名不能为空";
			return errorMsg;
		}
		if (nickname.length() > 10) {
			errorMsg = "姓名不能超过10个字符";
			return errorMsg;
		}
		if (!CheckUtil.notExistSpecial(nickname)) {
			errorMsg = "姓名不能包含特殊字符";
			return errorMsg;
		}
		if (!CheckUtil.checkString(nickname)) {
			errorMsg = "请输入正确的姓名";
			return errorMsg;
		}
		if (StringUtils.isEmpty(email)) {
			errorMsg = "邮箱不能为空";
			return errorMsg;
		}
		if (!CheckUtil.checkEmail(email)) {
			errorMsg = "请输入正确的邮箱";
			return errorMsg;
		}
		if (!CheckUtil.checkMobileFormat(mobile)) {
			errorMsg = "手机号码格式不正确";
			return errorMsg;
		}
		if (StringUtils.isEmpty(roles)) {
			errorMsg = "角色不能为空";
			return errorMsg;
		}
		if (null == department) {
			errorMsg = "请选择部门";
			return errorMsg;
		}
		Department dep = departmentService.findDepartmentById(department);
		if (null == dep) {
			errorMsg = "部门不存在";
			return errorMsg;
		}
		Long userCount = userService.countByUserName(userId, username);
		if (userCount > 0) {
			errorMsg = "用户名已存在";
			return errorMsg;
		}
		return errorMsg;
	}

}
