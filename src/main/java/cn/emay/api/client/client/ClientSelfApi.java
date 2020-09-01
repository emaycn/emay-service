package cn.emay.api.client.client;

import cn.emay.constant.redis.BusinessCacheKeys;
import cn.emay.constant.web.ResourceEnum;
import cn.emay.constant.web.WebAuth;
import cn.emay.core.client.pojo.Client;
import cn.emay.redis.RedisClient;
import cn.emay.utils.ApplicationContextUtils;
import cn.emay.utils.WebUtils;
import cn.emay.utils.number.BigDecimalUtils;
import cn.emay.utils.result.SuperResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 客户API
 *
 * @author wangyue
 */
@RestController
@Api(tags = {"我的客户信息"})
@RequestMapping(value = "/c/clientself", method = RequestMethod.POST)
public class ClientSelfApi {

    /**
     * 客户信息
     */
    @WebAuth({ResourceEnum.CLIENT_COMMPANY_INFO})
    @RequestMapping("/info")
    @ApiOperation("客户信息")
    public SuperResult<Client> userinfo() {
        Client client = WebUtils.getCurrentClient();
        if (client == null) {
            return SuperResult.badResult("信息不存在");
        }
        RedisClient redis = ApplicationContextUtils.getBean(RedisClient.class);
        Long balance = redis.get(BusinessCacheKeys.KV_CLIENT_BALANCE_ + client.getId(), Long.class);
        balance = balance == null ? 0 : balance;
        BigDecimal bd = BigDecimalUtils.div(new BigDecimal(balance), new BigDecimal(10000), 4);
        client.setBalance(bd);
        return SuperResult.rightResult(client);
    }

}
