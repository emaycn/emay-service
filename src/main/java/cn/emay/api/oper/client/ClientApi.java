package cn.emay.api.oper.client;

import cn.emay.constant.redis.BusinessCacheKeys;
import cn.emay.constant.web.ResourceEnum;
import cn.emay.constant.web.WebAuth;
import cn.emay.core.client.pojo.Client;
import cn.emay.core.client.pojo.ClientChargeRecord;
import cn.emay.core.client.service.ClientChargeRecordService;
import cn.emay.core.client.service.ClientService;
import cn.emay.core.system.pojo.User;
import cn.emay.core.system.service.UserOperLogService;
import cn.emay.redis.RedisClient;
import cn.emay.utils.CheckUtils;
import cn.emay.utils.WebUtils;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.number.BigDecimalUtils;
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
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

/**
 * 客户API
 *
 * @author wangyue
 */
@RestController
@Api(tags = {"客户管理"})
@RequestMapping(value = "/o/client", method = RequestMethod.POST)
public class ClientApi {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private ClientService clientService;
    @Resource
    private ClientChargeRecordService clientChargeRecordService;
    @Resource
    private UserOperLogService userOperLogService;
    @Resource
    private RedisClient redis;

    /**
     * 客户列表
     */
    @WebAuth({ResourceEnum.CLIENT_VIEW})
    @ApiOperation("客户列表")
    @RequestMapping("/list")
    @ApiImplicitParams({@ApiImplicitParam(name = "clientName", value = "客户名称", required = true, dataType = "string"),
            @ApiImplicitParam(name = "linkman", value = "联系人", dataType = "string"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "string"),
            @ApiImplicitParam(name = "start", value = "开始位置", dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "条数", dataType = "int"),})
    public SuperResult<Page<Client>> list(int start, int limit, String clientName, String linkman, String mobile) {
        Page<Client> clientPage = clientService.findPage(start, limit, clientName, linkman, mobile);
        clientPage.getList().forEach(client -> {
            Long balance = redis.get(BusinessCacheKeys.KV_CLIENT_BALANCE_ + client.getId(), Long.class);
            balance = balance == null ? 0 : balance;
            BigDecimal bd = BigDecimalUtils.div(new BigDecimal(balance), new BigDecimal(10000), 4);
            client.setBalance(bd);
        });
        return SuperResult.rightResult(clientPage);
    }

    /**
     * 模糊查询客户列表
     */
    @WebAuth({ResourceEnum.CLIENT_VIEW})
    @ApiOperation("模糊查询客户列表")
    @RequestMapping("/findbyname")
    @ApiImplicitParams({@ApiImplicitParam(name = "clientName", value = "客户名称", required = true, dataType = "string")})
    public SuperResult<List<Client>> findByName(String clientName) {
        List<Client> clientList = clientService.findByName(clientName);
        return SuperResult.rightResult(clientList);
    }

    /**
     * 客户添加
     */
    @WebAuth({ResourceEnum.CLIENT_ADD})
    @ApiOperation("客户添加")
    @RequestMapping("/add")
    @ApiImplicitParams({@ApiImplicitParam(name = "clientName", value = "客户名称", required = true, dataType = "string"),
            @ApiImplicitParam(name = "linkman", value = "联系人", dataType = "string"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "string"),
            @ApiImplicitParam(name = "email", value = "email", dataType = "string"),
            @ApiImplicitParam(name = "address", value = "地址", dataType = "string"),})
    public Result add(String clientName, String linkman, String mobile, String email, String address, Boolean isBalanceWarning) {
        User user = WebUtils.getCurrentUser();
        String message = checkClientRequired(clientName, linkman, mobile, email, null);
        if (message != null) {
            return Result.badResult(message);
        }
        if (isBalanceWarning == null) {
            isBalanceWarning = true;
        }
        Client client = new Client(null, clientName, linkman, mobile, email, address, new Date(), user.getId(),
                new BigDecimal(0), isBalanceWarning);
        clientService.add(client);
        String context = "新增客户:客户名称为{0}";
        String module = "客户信息";
        userOperLogService.saveOperLog(module, MessageFormat.format(context, clientName));
        log.info("【客户信息】-->用户:" + user.getUsername() + "新增客户:客户名称为" + clientName);
        return Result.rightResult();
    }

    /**
     * 客户信息
     */
    @WebAuth({ResourceEnum.CLIENT_VIEW})
    @RequestMapping("/info")
    @ApiOperation("客户信息")
    public SuperResult<Client> userinfo(@ApiParam(name = "id", value = "客户ID", required = true) @RequestParam Long id) {
        if (id == null) {
            return SuperResult.badResult("客户不存在");
        }
        Client client = clientService.findById(id);
        if (client == null) {
            return SuperResult.badResult("客户不存在");
        }
        return SuperResult.rightResult(client);
    }

    /**
     * 修改客户
     */
    @WebAuth({ResourceEnum.CLIENT_MODIFY})
    @RequestMapping("/modify")
    @ApiOperation("修改客户")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "客户ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "clientName", value = "客户名称", required = true, dataType = "string"),
            @ApiImplicitParam(name = "linkman", value = "联系人", dataType = "string"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "string"),
            @ApiImplicitParam(name = "email", value = "email", dataType = "string"),
            @ApiImplicitParam(name = "address", value = "地址", dataType = "string"),})
    public Result modify(Long id, String clientName, String linkman, String mobile, String email, String address, Boolean isBalanceWarning) {
        Client client = clientService.findById(id);
        if (client == null) {
            return Result.badResult("客户不存在");
        }
        String message = checkClientRequired(clientName, linkman, mobile, email, client.getId());
        if (message != null) {
            return Result.badResult(message);
        }
        if (isBalanceWarning == null) {
            isBalanceWarning = true;
        }
        User user = WebUtils.getCurrentUser();
        client.setClientName(clientName);
        client.setLinkman(linkman);
        client.setMobile(mobile);
        client.setEmail(email);
        client.setAddress(address);
        client.setIsBalanceWarning(isBalanceWarning);
        clientService.modify(client);
        String context = "修改客户:客户名称为{0}";
        String module = "客户信息";
        userOperLogService.saveOperLog(module, MessageFormat.format(context, clientName));
        log.info("【客户信息】-->用户:" + user.getUsername() + "修改客户:客户名称为" + clientName);
        return Result.rightResult();
    }

    /**
     * 充值
     */
    @WebAuth({ResourceEnum.RECHARGE_CHARGE})
    @RequestMapping("/recharge")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "客户ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "changeBalance", value = "充值金额(充值金额整数位最多9位，小数位4位)", required = true, dataType = "string"),
            @ApiImplicitParam(name = "remark", value = "备注", dataType = "string"),})
    @ApiOperation("客户充值")
    public Result recharge(Long id, String changeBalance, String remark) {
        if (null == id) {
            return Result.badResult("客户ID不能为空.");
        }
        Client client = clientService.findById(id);
        if (client == null) {
            return Result.badResult("客户不存在");
        }
        if (!StringUtils.isEmpty(remark) && remark.length() > 50) {
            return Result.badResult("备注不能超过50个字符");
        }
        if (!CheckUtils.checkAmountOfMoney(changeBalance)) {
            return Result.badResult("充值金额位数错误(整数位最多9位，小数位4位).");
        }
        User user = WebUtils.getCurrentUser();
        Result result = clientService.modifyBalance(id, new BigDecimal(changeBalance));
        if (!result.getSuccess()) {
            return result;
        }
        // 添加充值记录
        ClientChargeRecord clientChargeRecord = new ClientChargeRecord(id, ClientChargeRecord.CHARGE_TYPE_RECHARGE,
                new BigDecimal(changeBalance), new Date(), user.getId(), remark);
        clientChargeRecordService.add(clientChargeRecord);
        String module = "客户管理";
        String context = "客户充值:{0},金额为:{1},备注:{2}";
        userOperLogService.saveOperLog(module, MessageFormat.format(context, client.getId(), changeBalance, remark));
        log.info("【客户管理】-->用户:" + user.getUsername() + ",客户id为" + client.getId() + "充值,金额为" + changeBalance + ",备注："
                + remark);
        return Result.rightResult();
    }

    /**
     * 扣费
     */
    @WebAuth({ResourceEnum.RECHARGE_CHARGE})
    @RequestMapping("/charge")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "客户ID", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "changeBalance", value = "扣费条数(扣费金额整数位最多9位，小数位4位)", required = true, dataType = "string"),
            @ApiImplicitParam(name = "remark", value = "备注", dataType = "string"),})
    @ApiOperation("客户扣费")
    public Result charge(Long id, String changeBalance, String remark) {
        if (null == id) {
            return Result.badResult("客户ID不能为空.");
        }
        Client client = clientService.findById(id);
        if (client == null) {
            return Result.badResult("客户不存在");
        }
        if (!StringUtils.isEmpty(remark) && remark.length() > 50) {
            return Result.badResult("备注不能超过50个字符");
        }
        if (!CheckUtils.checkAmountOfMoney(changeBalance)) {
            return Result.badResult("扣费金额位数错误(整数位最多9位，小数位4位).");
        }
        User user = WebUtils.getCurrentUser();
        Result result = clientService.modifyBalance(id,
                BigDecimalUtils.mul(new BigDecimal(-1), new BigDecimal(changeBalance)));
        if (!result.getSuccess()) {
            return result;
        }
        // 添加充值记录
        ClientChargeRecord clientChargeRecord = new ClientChargeRecord(id, ClientChargeRecord.CHARGE_TYPE_CHARGE,
                BigDecimalUtils.mul(new BigDecimal(-1), new BigDecimal(changeBalance)), new Date(), user.getId(),
                remark);
        clientChargeRecordService.add(clientChargeRecord);
        String module = "客户管理";
        String context = "客户扣费:{0},金额为:{1},备注:{2}";
        userOperLogService.saveOperLog(module, MessageFormat.format(context, client.getId(), changeBalance, remark));
        log.info("【客户管理】-->用户:" + user.getUsername() + ",客户id为" + client.getId() + "扣费,金额为" + changeBalance + ",备注："
                + remark);
        return Result.rightResult();
    }

    /**
     * 校验客户信息
     *
     * @param clientName 姓名
     * @param mobile     手机号
     * @param email      邮箱
     * @param linkname   联系人
     * @return 错误信息
     */
    private String checkClientRequired(String clientName, String linkname, String mobile, String email, Long clientId) {
        if (StringUtils.isEmpty(clientName)) {
            return "客户名称不能为空！";
        }
        if (clientName.length() > 50) {
            return "客户名称长度不能大于50个字符";
        }
        // 校验客户名唯一
        Client client = clientService.findByClientName(clientName, clientId);
        if (client != null) {
            return "客户名已存在！";
        }
        if (StringUtils.isEmpty(linkname)) {
            return "联系人不能为空";
        }
        if (linkname.length() > 10) {
            return "联系人不能超过10个字符";
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
        return null;
    }

}
