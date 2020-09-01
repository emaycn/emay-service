package cn.emay.core.client.dao;

import cn.emay.core.client.pojo.Client;
import cn.emay.orm.BaseSuperDao;
import cn.emay.utils.db.common.Page;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * cn.emay.common.pojo.client.Client Dao super
 *
 * @author frank
 */
public interface ClientDao extends BaseSuperDao<Client> {
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
     * 根據用戶id查詢客戶
     *
     * @param userId 用戶ID
     * @return
     */
    Client findByUserId(Long userId);

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

    /**
     * 根据用户id查询客户名
     *
     * @param userIds 用户ids
     * @return
     */
    Map<Long, String> findClientNameByUserIds(Set<Long> userIds);

    List<Client> findbyIds(Long[] clientIds);
}