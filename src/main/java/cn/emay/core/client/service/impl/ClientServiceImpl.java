package cn.emay.core.client.service.impl;

import cn.emay.constant.redis.BusinessCacheKeys;
import cn.emay.core.client.dao.ClientDao;
import cn.emay.core.client.pojo.Client;
import cn.emay.core.client.service.ClientService;
import cn.emay.redis.RedisClient;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.number.BigDecimalUtils;
import cn.emay.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * cn.emay.common.pojo.client.Client Service implement
 *
 * @author frank
 */
@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientDao clientDao;
    @Autowired
    private RedisClient redis;

    @Override
    public Page<Client> findPage(int start, int limit, String clientName, String linkman, String mobile) {
        return clientDao.findPage(start, limit, clientName, linkman, mobile);
    }

    @Override
    public Result add(Client client) {
        clientDao.save(client);
        return Result.rightResult();
    }

    @Override
    public Client findById(Long clientId) {
        return clientDao.findById(clientId);
    }

    @Override
    public Result modify(Client client) {
        clientDao.update(client);
        return Result.rightResult();
    }

    @Override
    public Result modifyBalance(Long clientId, BigDecimal balance) {
        Long balanceLong = BigDecimalUtils.mul(balance, new BigDecimal(10000), 0).longValue();
        Long balanceCheck = redis.get(BusinessCacheKeys.KV_CLIENT_BALANCE_ + clientId, Long.class);
        if (null == balanceCheck) {
            balanceCheck = 0L;
        }
        if (0L > balanceLong + balanceCheck) {
            return Result.badResult("余额不足！");
        }
        Long result = redis.incrBy(BusinessCacheKeys.KV_CLIENT_BALANCE_ + clientId, balanceLong);
        if (0L > result) {
            redis.incrBy(BusinessCacheKeys.KV_CLIENT_BALANCE_ + clientId, -balanceLong);
            return Result.badResult("余额不足！");
        }
        return Result.rightResult();
    }

    @Override
    public Client findByUserId(Long userId) {
        return clientDao.findByUserId(userId);
    }

    @Override
    public List<Client> findByName(String clientName) {
        return clientDao.findByName(clientName);
    }

    @Override
    public Client findByClientName(String clientName, Long clientId) {
        return clientDao.findByClientName(clientName, clientId);
    }

    @Override
    public List<Client> findbyIds(Long[] clientIds) {
        return clientDao.findbyIds(clientIds);
    }

}