package cn.emay.boot.business.system.api;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cn.emay.boot.business.system.pojo.Resource;
import cn.emay.boot.business.system.pojo.Role;
import cn.emay.boot.business.system.pojo.RoleResourceAssign;
import cn.emay.boot.business.system.pojo.UserOperLog;
import cn.emay.boot.business.system.service.ResourceService;
import cn.emay.boot.business.system.service.RoleService;
import cn.emay.boot.business.system.service.UserOperLogService;
import cn.emay.boot.business.system.service.UserRoleAssignService;
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
 * 角色API
 * 
 * @author frank
 *
 */
@RequestMapping(value = "/role", method = RequestMethod.POST)
@RestController
@Api(tags = { "角色管理" })
public class RoleApi {

	Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private RoleService roleService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private UserRoleAssignService userRoleAssignService;
	@Autowired
	private UserOperLogService userOperLogService;

	/**
	 * 角色列表
	 */
	@WebAuth({ ResourceEnum.ROLE_VIEW })
	@RequestMapping("/page")
	@ApiOperation("分页查询角色列表")
	@ApiImplicitParams({ @ApiImplicitParam(name = "start", value = "起始数据位置", required = true, dataType = "int"), @ApiImplicitParam(name = "limit", value = "数据条数", required = true, dataType = "int"),
			@ApiImplicitParam(name = "roleName", value = "角色名称", dataType = "string"), })
	public SuperResult<Page<Role>> rolelist(int start, int limit, String roleName) {
		Page<Role> userpage = roleService.findPage(start, limit, roleName);
		return SuperResult.rightResult(userpage);
	}

	/**
	 * 添加角色
	 */
	@WebAuth({ ResourceEnum.ROLE_ADD })
	@RequestMapping("/add")
	@ApiOperation("添加角色")
	@ApiImplicitParams({ @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "string"),
			@ApiImplicitParam(name = "remark", value = "角色描述", required = true, dataType = "string"),
			@ApiImplicitParam(name = "resourceCodes", value = "角色编码权限(逗号分割)", required = true, dataType = "string"), })
	public Result add(String roleName, String remark, String resourceCodes) {
		List<RoleResourceAssign> roleList = new ArrayList<RoleResourceAssign>();
		String errorMsg = checkData(roleName, resourceCodes, remark, null, roleList);
		if (!StringUtils.isEmpty(errorMsg)) {
			return Result.badResult(errorMsg);
		}
		roleService.add(roleName, remark, roleList);
		String context = "添加角色:{0}";
		String module = "角色管理";
		userOperLogService.saveLogByCurrentUser(module, MessageFormat.format(context, new Object[] { roleName }), UserOperLog.OPERATE_ADD);
		return Result.rightResult();
	}

	/**
	 * 修改角色权限
	 */
	@ApiOperation("修改角色权限")
	@WebAuth({ ResourceEnum.ROLE_MODIFY })
	@RequestMapping("/modify")
	@ApiImplicitParams({ @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "Long"),
			@ApiImplicitParam(name = "roleName", value = "角色名称", required = true, dataType = "string"), @ApiImplicitParam(name = "remark", value = "角色描述", required = true, dataType = "string"),
			@ApiImplicitParam(name = "resourceCodes", value = "角色编码权限(逗号分割)", required = true, dataType = "string"), })
	public Result modify(Long roleId, String roleName, String remark, String resourceCodes) {
		if (roleId == 1L) {
			return Result.badResult("管理员角色不能修改");
		}
		List<RoleResourceAssign> roleList = new ArrayList<RoleResourceAssign>();
		String errorMsg = checkData(roleName, resourceCodes, remark, roleId, roleList);
		if (!StringUtils.isEmpty(errorMsg)) {
			return Result.badResult(errorMsg);
		}
		roleService.modify(roleId, roleName, remark, roleList);
		String context = "修改角色:{0}";
		String module = "角色管理";
		userOperLogService.saveLogByCurrentUser(module, MessageFormat.format(context, new Object[] { roleName }), UserOperLog.OPERATE_MODIFY);
		return Result.rightResult();
	}

	/**
	 * 角色所有资源
	 */
	@WebAuth({ ResourceEnum.ROLE_VIEW })
	@RequestMapping(value = "/roleResource")
	@ApiOperation("角色所有资源")
	public SuperResult<List<Resource>> roleResource(@ApiParam(name = "roleId", value = "角色ID", required = true) @RequestParam Long roleId) {
		List<Resource> userResource = resourceService.getRoleResources(roleId);
		return SuperResult.rightResult(userResource);
	}

	/**
	 * 删除角色
	 */
	@WebAuth({ ResourceEnum.ROLE_DELETE })
	@RequestMapping("/delete")
	@ApiOperation("删除角色")
	public Result delete(@ApiParam(name = "roleId", value = "角色ID", required = true) @RequestParam Long roleId) {
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
		userOperLogService.saveLogByCurrentUser(module, MessageFormat.format(context, new Object[] { role.getRoleName() }), UserOperLog.OPERATE_DELETE);
		return Result.rightResult();
	}

	/**
	 * 检测新增/修改数据
	 * 
	 * @param roleName
	 *            角色名
	 * @param auths
	 *            权限集合
	 * @param remark
	 *            备注
	 * @param roleId
	 *            角色ID
	 * @param roleList
	 *            权限接收容器
	 * @return
	 */
	private String checkData(String roleName, String auths, String remark, Long roleId, List<RoleResourceAssign> roleList) {
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
		List<Resource> allResource = resourceService.getAll();
		Map<String, Long> map = new HashMap<String, Long>();
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
