package cn.emay.api.oper.client;

import cn.emay.constant.web.ResourceEnum;
import cn.emay.constant.web.WebAuth;
import cn.emay.core.client.dto.ClientChargeRecordDTO;
import cn.emay.core.client.service.ClientChargeRecordService;
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
import java.util.Date;

/**
 * 客户账务API
 *
 * @author chang
 */
@RestController
@Api(tags = {"账务管理"})
@RequestMapping(value = "/o/charge", method = RequestMethod.POST)
public class ClientChargeApi {

    @Resource
    private ClientChargeRecordService clientChargeRecordService;

    /**
     * 充值扣费列表
     */
    @WebAuth({ResourceEnum.CLIENT_CHARGE})
    @ApiOperation("账务明细")
    @RequestMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "clientName", value = "客户名称", dataType = "string"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "date"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "date"),
            @ApiImplicitParam(name = "start", value = "开始位置", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "条数", dataType = "int"),})
    public SuperResult<Page<ClientChargeRecordDTO>> list(int start, int limit, String clientName, Date startTime,
                                                         Date endTime) {
        Page<ClientChargeRecordDTO> clientChargePage = clientChargeRecordService.findChargePage(start, limit,
                clientName, startTime, endTime);
        return SuperResult.rightResult(clientChargePage);
    }

}
