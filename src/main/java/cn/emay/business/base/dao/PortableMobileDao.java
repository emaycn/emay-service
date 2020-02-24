package cn.emay.business.base.dao;

import java.util.List;

import cn.emay.business.base.pojo.PortableMobile;
import cn.emay.orm.BaseSuperDao;
import cn.emay.utils.db.common.Page;

/**
 * cn.emay.common.pojo.base.PortableMobile Dao super
 *
 * @author frank
 */
public interface PortableMobileDao extends BaseSuperDao<PortableMobile> {
    /**
     * 分页查询
     *
     * @param mobile 手机号
     * @param start  从第几条开始查
     * @param limit  查几条
     * @return
     */
    Page<PortableMobile> findPage(int start, int limit, String mobile);

    PortableMobile findByMobile(String mobile);

    void updateIsdelete(List<Long> ids);

    void saveByAutoNamed(List<PortableMobile> portableMobiles);
}