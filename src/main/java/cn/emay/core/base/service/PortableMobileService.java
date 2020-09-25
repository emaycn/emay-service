package cn.emay.core.base.service;

import cn.emay.core.base.pojo.PortableMobile;
import cn.emay.utils.db.common.Page;

import java.util.List;

/**
 * cn.emay.common.pojo.base.PortableMobile Service Super
 *
 * @author frank
 */
public interface PortableMobileService {
    /**
     * 分页查询
     *
     * @param mobile 手机号
     * @param start  从第几条开始查
     * @param limit  查几条
     * @return 分页数据
     */
    Page<PortableMobile> findPage(int start, int limit, String mobile);

    void save(PortableMobile portableMobile);

    PortableMobile findbyId(Long id);

    void update(PortableMobile portableMobile);

    void delete(PortableMobile portableMobile);

    void saveBatch(List<PortableMobile> portableMobiles);

    PortableMobile findByMobile(String mobile);
}