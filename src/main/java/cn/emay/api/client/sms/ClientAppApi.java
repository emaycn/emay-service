package cn.emay.api.client.sms;

import cn.emay.constant.web.ResourceEnum;
import cn.emay.constant.web.WebAuth;
import cn.emay.core.client.pojo.Client;
import cn.emay.core.sms.dto.AppDto;
import cn.emay.core.sms.service.SmsAppService;
import cn.emay.utils.WebUtils;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.SuperResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 客户应用API
 *
 * @author chang
 */
@RestController
@Api(tags = {"应用"})
@RequestMapping(value = "/c/clientapp", method = RequestMethod.POST)
public class ClientAppApi {

    @Resource
    private SmsAppService appService;

    /**
     * 应用列表
     */
    @WebAuth({ResourceEnum.CLIENT_APP_VIEW})
    @ApiOperation("应用列表")
    @RequestMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "appKey", value = "appKey", dataType = "string"),
            @ApiImplicitParam(name = "appCode", value = "服务号", dataType = "string"), @ApiImplicitParam(name = "state", value = "状态", dataType = "int"),
            @ApiImplicitParam(name = "start", value = "开始位置", dataType = "int"), @ApiImplicitParam(name = "limit", value = "条数", dataType = "int"),})
    public SuperResult<Page<AppDto>> page(int start, int limit, String clientName, String appKey, String appCode, Integer state) {
        Client client = WebUtils.getCurrentClient();
        Page<AppDto> appPage = appService.findClientPage(start, limit, client.getId(), appKey, appCode, state);
        return SuperResult.rightResult(appPage);
    }

    /**
     * 查询公司信息
     */
    @WebAuth({ResourceEnum.CLIENT_COMMPANY_INFO})
    @ApiOperation("查询应用参数信息")
    @RequestMapping("/clientInfo")
    @ApiImplicitParams({@ApiImplicitParam()})
    public SuperResult<Client> clientInfo() {
        Client client = WebUtils.getCurrentClient();
        return SuperResult.rightResult(client);
    }

    /**
     * 客户所有应用列表
     */
    @WebAuth({ResourceEnum.CLIENT_APP_VIEW})
    @ApiOperation("客户所有应用列表")
    @RequestMapping("/allapplist")
    @ApiImplicitParams({})
    public SuperResult<List<AppDto>> allapplist() {
        Client client = WebUtils.getCurrentClient();
        List<AppDto> appPage = appService.findAllAppList(client.getId());
        return SuperResult.rightResult(appPage);
    }

}
