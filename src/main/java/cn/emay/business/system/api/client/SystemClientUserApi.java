package cn.emay.business.system.api.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.emay.business.client.pojo.Client;
import cn.emay.business.system.dto.UserItemDTO;
import cn.emay.business.system.service.UserService;
import cn.emay.constant.web.ResourceEnum;
import cn.emay.constant.web.WebAuth;
import cn.emay.utils.WebUtils;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.SuperResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 用户API
 *
 * @author chang
 */
@RestController
@RequestMapping(value = "/c/systemclientuser", method = RequestMethod.POST)
@Api(tags = {"用户管理"})
public class SystemClientUserApi {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    /**
     * 用户列表
     */
    @WebAuth({ResourceEnum.CLIENT_USER_VIEW})
    @RequestMapping("/page")
    @ApiOperation("分页查询用户列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "start", value = "起始数据位置", required = true, dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "数据条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "string"),
            @ApiImplicitParam(name = "realname", value = "姓名", dataType = "string"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "string"),
            @ApiImplicitParam(name = "userState", value = "用戶状态", dataType = "int")})
    public SuperResult<Page<UserItemDTO>> list(int start, int limit, String username, String realname, String mobile, Integer userState) {
        Client client = WebUtils.getCurrentClient();
        Page<UserItemDTO> userpage = userService.findClientPage(start, limit, username, realname, mobile, userState, client.getId());
        log.info("user : " + WebUtils.getCurrentUser().getUsername() + " select user page.");
        return SuperResult.rightResult(userpage);
    }


  
}
