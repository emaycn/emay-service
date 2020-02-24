package cn.emay.business.base.dao;

import java.util.List;

import cn.emay.business.base.pojo.EmptyMobile;
import cn.emay.orm.BaseSuperDao;
import cn.emay.utils.db.common.Page;

/**
 * cn.emay.common.pojo.base.EmptyMobile Dao super
 *
 * @author frank
 */
public interface EmptyMobileDao extends BaseSuperDao<EmptyMobile> {
    /**
     * 分页查询
     *
     * @param mobile 手机号
     * @param start  从第几条开始查
     * @param limit  查几条
     * @return
     */
    Page<EmptyMobile> findPage(int start, int limit, String mobile);

    EmptyMobile findByMobile(String mobile);

    void updateIsdelete(List<Long> ids);

    void saveByAutoNamed(List<EmptyMobile> emptyMobiles);

}