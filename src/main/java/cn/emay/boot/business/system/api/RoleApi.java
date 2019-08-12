package cn.emay.boot.business.system.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
import cn.emay.boot.business.system.pojo.User;
import cn.emay.boot.business.system.pojo.UserOperLog;
import cn.emay.boot.business.system.service.ResourceService;
import cn.emay.boot.business.system.service.RoleService;
import cn.emay.boot.business.system.service.UserOperLogService;
import cn.emay.boot.utils.CheckUtil;
import cn.emay.boot.utils.WebUtils;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.Result;
import cn.emay.utils.result.SuperResult;

/**
 * 角色管理
 * 
 * @author 东旭
 *
 */
@RequestMapping(value = "/role", method = RequestMethod.POST)
@RestController
@Api(tags={"角色管理"})
public class RoleApi {

	Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private RoleService roleService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private UserOperLogService userOperLogService;
	
	/**
	 * 角色列表
	 */
	@WebAuth({ ResourceEnum.ROLE_VIEW })
	@RequestMapping("/page")
	@ApiOperation("分页查询角色列表")
	@ApiImplicitParams({
		@ApiImplicitParam(name="start", value="起始数据位置",required=true, dataType="int"),
		@ApiImplicitParam(name="limit", value="数据条数",required=true, dataType="int"),
		@ApiImplicitParam(name="roleName", value="角色名称",dataType="string"),
	})
	public SuperResult<Page<Role>> rolelist(int start, int limit,String roleName) {
		Page<Role> userpage = roleService.findPage(start, limit,roleName);
		return SuperResult.rightResult(userpage);
	}
	
	
	/**
	 * 添加角色
	 */
	@WebAuth({ ResourceEnum.ROLE_ADD })
	@RequestMapping("/add")
	@ApiOperation("添加角色")
	@ApiImplicitParams({
		@ApiImplicitParam(name="roleName", value="角色名称",required=true, dataType="string"),
		@ApiImplicitParam(name="remark", value="角色描述",required=true, dataType="string"),
		@ApiImplicitParam(name="resourceIds", value="角色权限",required=true,dataType="string"),
	})
	public Result add(String roleName, String remark, String resourceIds) {
		List<RoleResourceAssign> roleList = new ArrayList<RoleResourceAssign>();
		String errorMsg=checkData(roleName,resourceIds,remark,null,roleList);
		if(!StringUtils.isEmpty(errorMsg)){
			return Result.badResult(errorMsg);
		}
		roleService.add(roleName, remark, roleList);
		User currentUser = WebUtils.getCurrentUser();
		String context = "添加角色:{0}";
		String module = "角色管理";
		userOperLogService.saveLog(module, currentUser.getId(),currentUser.getUsername(), MessageFormat.format(context, new Object[] {roleName}), UserOperLog.OPERATE_ADD);
		log.info("用户:"+currentUser.getUsername()+"添加角色,角色名称为:"+roleName);
		return Result.rightResult();
	}

	/**
	 * 修改角色权限
	 * 
	 * @return
	 */
	@ApiOperation("修改角色权限")
	@WebAuth({ ResourceEnum.ROLE_MODIFY })
	@RequestMapping("/modify")
	@ApiImplicitParams({
		@ApiImplicitParam(name="roleId", value="角色ID",required=true, dataType="Long"),
		@ApiImplicitParam(name="roleName", value="角色名称",required=true, dataType="string"),
		@ApiImplicitParam(name="remark", value="角色描述",required=true, dataType="string"),
		@ApiImplicitParam(name="resourceIds", value="角色权限",required=true,dataType="string"),
	})
	public Result modify(Long roleId, String roleName, String remark, String resourceIds) {
		List<RoleResourceAssign> roleList = new ArrayList<RoleResourceAssign>();
		String errorMsg=checkData(roleName,resourceIds,remark,roleId,roleList);
		if(!StringUtils.isEmpty(errorMsg)){
			return Result.badResult(errorMsg);
		}
		roleService.modify(roleId, roleName, remark, roleList);
		User currentUser = WebUtils.getCurrentUser();
		String context = "修改角色:{0}";
		String module = "角色管理";
		userOperLogService.saveLog(module, currentUser.getId(),currentUser.getUsername(), MessageFormat.format(context, new Object[] {roleName}), UserOperLog.OPERATE_MODIFY);
		log.info("用户:"+currentUser.getUsername()+"修改角色,角色名称为:"+roleName);
		return Result.rightResult();
	}

	/**
	 * 删除角色
	 */
	@WebAuth({ ResourceEnum.ROLE_DELETE })
	@RequestMapping("/delete")
	@ApiOperation("删除角色")
	public Result delete(@ApiParam(name="roleId",value="角色ID",required=true)@RequestParam Long roleId) {
		if (roleId == null || roleId == 0l) {
			return Result.badResult("角色不存在");
		}
		if (roleId == 1l) {
			return Result.badResult("管理员角色不能删除");
		}
		Role role = roleService.findById(roleId);
		if(role == null) {
			return Result.badResult("角色不存在");
		}
		roleService.delete(roleId);
		User currentUser = WebUtils.getCurrentUser();
		String context = "删除角色:{0}";
		String module = "角色管理";
		userOperLogService.saveLog(module, currentUser.getId(),currentUser.getUsername(), MessageFormat.format(context, new Object[] {role.getRoleName()}), UserOperLog.OPERATE_DELETE);
		log.info("用户:"+currentUser.getUsername()+"删除角色,角色名称为:"+role.getRoleName());
		return Result.rightResult();
	}
	
	/**
	 * 角色所有资源
	 */
	@WebAuth({ ResourceEnum.ROLE_VIEW })
	@RequestMapping(value = "/roleResource")
	@ApiOperation("角色所有资源")
	public SuperResult<List<Resource>> roleResource(@ApiParam(name="roleId",value="角色ID",required=true)@RequestParam Long roleId) {
		List<Resource> userResource = resourceService.getRoleResources(roleId);
		return SuperResult.rightResult(userResource);
	}

	/**
	 * 系统所有角色
	 */
	@WebAuth({ ResourceEnum.ROLE_VIEW })
	@RequestMapping("/allRole")
	@ApiOperation("系统所有角色")
	public SuperResult<List<Role>> allRoles() {
		List<Role> roles = roleService.findAll();
		return SuperResult.rightResult(roles);
	}

	/**
	 * 系统所有资源
	 */
	@WebAuth({ ResourceEnum.ROLE_VIEW })
	@RequestMapping(value = "/allResource")
	@ApiOperation("系统所有资源")
	public SuperResult<List<Resource>> allresource() {
		List<Resource> allResource = resourceService.getAll();
		return SuperResult.rightResult(allResource);
	}
	
	/**
	 * 用户所有角色
	 */
	@WebAuth({ ResourceEnum.ROLE_VIEW })
	@RequestMapping("/userRole")
	@ApiOperation("用户所有角色")
	public SuperResult<List<Role>> userRoles(@ApiParam(name="userId",value="用户ID",required=true)@RequestParam Long userId) {
		List<Role> roles = roleService.getUserRoles(userId);
		return SuperResult.rightResult(roles);
	}
	
	/**
	 * 当前用户所有资源
	 */
	@WebAuth({ ResourceEnum.ROLE_VIEW })
	@RequestMapping(value = "/myResource")
	@ApiOperation("当前用户所有资源")
	public SuperResult<List<Resource>> userresource() {
		User user = WebUtils.getCurrentUser();
		List<Resource> userResource = resourceService.getUserResources(user.getId());
		return SuperResult.rightResult(userResource);
	}
	
	private String checkData(String roleName,String auths,String remark,Long roleId,List<RoleResourceAssign> roleList){
		String errorMsg = "";
		if (StringUtils.isEmpty(roleName)) {
			errorMsg = "角色名不能为空";
			return errorMsg;
		}
		if (StringUtils.isEmpty(auths)) {
			errorMsg = "权限不能为空";
			return errorMsg;
		}
		if (roleName.length() > 20) {
			errorMsg = "角色名称长度不可超过20个字符";
			return errorMsg;
		}
		if (!StringUtils.isEmpty(remark)&&remark.length() > 50) {
			errorMsg = "角色描述不能超过50个字符";
			return errorMsg;
		}
		if (!CheckUtil.notExistSpecial(roleName)) {
			errorMsg = "角色名称不合法";
			return errorMsg;
		}
		if(null!=roleId){
			Role role = roleService.findById(roleId);
			if(role == null) {
				errorMsg = "角色不存在";
				return errorMsg;
			}
		}
		Long count = roleService.countNumberByRoleName(roleName, roleId);
		// 角色名称唯一校验
		if (count > 0) {
			errorMsg = "角色名已存在";
			return errorMsg;
		}
		//校验权限
		List<Resource> allResource = resourceService.getAll();
		Map<Long,String> map = new HashMap<Long, String>();
		for(Resource resource:allResource){
			map.put(resource.getId(), "");
		}
		try {
			String[] aus = auths.split(",");
			for (String au : aus) {
				if (!map.containsKey(Long.valueOf(au))) {
					errorMsg = "权限不存在";
					return errorMsg;
				}
				RoleResourceAssign roleResourceAssign = new RoleResourceAssign(roleId, Long.valueOf(au));
				roleList.add(roleResourceAssign);
			}
		} catch (Exception e) {
			log.error("权限解析错误",e);
			errorMsg = "权限解析错误";
			return errorMsg;
		}
		return errorMsg;
	}
	
}
