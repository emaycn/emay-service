package cn.emay.core.client.service;

import cn.emay.core.client.pojo.Client;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.Result;

import java.math.BigDecimal;
import java.util.List;

/**
 * cn.emay.common.pojo.client.Client Service Super
 *
 * @author frank
 */
public interface ClientService {

    /**
     * 分页查询客户
     *
     * @param start      其实数据位置
     * @param limit      查询的数据条数
     * @param clientName 客户名
     * @param linkman    联系人
     * @param mobile     手机号
     * @return
     */
    Page<Client> findPage(int start, int limit, String clientName, String linkman, String mobile);

    /**
     * 保存客户
     */
    /**
     * 新增客户<br/>
     *
     * @param client 客户
     * @return
     */
    Result add(Client client);

    /**
     * 按照ID查找客户
     *
     * @param clientId 客户ID
     * @return
     */
    Client findById(Long clientId);

    /**
     * 修改客户<br/>
     *
     * @param client 客户
     * @return
     */
    Result modify(Client client);

    /**
     * 充值扣费<br/>
     *
     * @param clientId 客户ID
     * @param balance  余额
     * @return
     */
    Result modifyBalance(Long clientId, BigDecimal balance);

    /**
     * 根據用戶id查詢客戶
     *
     * @param id 用戶ID
     * @return
     */
    Client findByUserId(Long id);

    /**
     * 根据客户名字模糊查询10条数据
     *
     * @param clientName 客户ming
     * @return
     */
    List<Client> findByName(String clientName);

    /**
     * 检查客户名是否存在
     *
     * @param clientName 客户ming
     * @param clientId
     * @return
     */
    Client findByClientName(String clientName, Long clientId);

    List<Client> findbyIds(Long[] clientIds);
}