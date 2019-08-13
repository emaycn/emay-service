package cn.emay.boot.business.system.api;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.emay.boot.base.constant.ResourceEnum;
import cn.emay.boot.base.web.WebAuth;
import cn.emay.boot.business.system.pojo.UserOperLog;
import cn.emay.boot.business.system.service.UserOperLogService;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.SuperResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
* @项目名称：ebdp-web-operation 
* @类描述： 日志管理  
* @创建人：lijunjian   
* @创建时间：2019年7月5日 下午2:51:54   
* @修改人：lijunjian   
* @修改时间：2019年7月5日 下午2:51:54   
* @修改备注：
 */
@RequestMapping(value="/user/log",method=RequestMethod.POST)
@RestController
@Api(tags={"日志管理"})
public class UserOperLogApi {

	@Autowired
	private UserOperLogService userOperLogService;
	
//	@InitBinder
//    public void initBinder(WebDataBinder binder, WebRequest request) {
//        //转换日期 注意这里的转化要和传进来的字符串的格式一直 如2015-9-9 就应该为yyyy-MM-dd
//        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));// CustomDateEditor为自定义日期编辑器
//    }
	/**
	 * 日志列表
	 */
	@WebAuth({ ResourceEnum.USER_VIEW })
	@RequestMapping("/page")
	@ApiOperation("分页查询日志列表")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "start", value = "起始数据位置",required = true,dataType = "int"),
        @ApiImplicitParam(name = "limit", value = "数据条数", required = true, dataType = "int"),
        @ApiImplicitParam(name = "username", value = "操作用户", dataType = "String"),
        @ApiImplicitParam(name = "content", value = "内容", dataType = "String"),
        @ApiImplicitParam(name = "startDate", value = "操作开始时间",dataType = "Date"),
        @ApiImplicitParam(name = "endDate", value = "操作结束时间", dataType = "Date"),
	})
	public SuperResult<Page<UserOperLog>> page(String username, String content, Date startDate, Date endDate, int start, int limit) {
		Page<UserOperLog> userpage = userOperLogService.findByPage(username, content, startDate, endDate, start, limit);
		return SuperResult.rightResult(userpage);
	}
}
