package cn.emay.business.client.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.business.client.service.ClientUserService;

/**
 * cn.emay.common.pojo.client.ClientUser Service implement
 *
 * @author chang
 */
@Service
public class ClientUserServiceImpl implements ClientUserService {

    @Resource
    private ClientUserService clientUserService;

}