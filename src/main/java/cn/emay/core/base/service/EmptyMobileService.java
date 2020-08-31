package cn.emay.core.base.service;

import cn.emay.core.base.pojo.EmptyMobile;
import cn.emay.utils.db.common.Page;

import java.util.List;

/**
 * cn.emay.common.pojo.base.EmptyMobile Service Super
 *
 * @author frank
 */
public interface EmptyMobileService {
    /**
     * 分页查询
     *
     * @param mobile 手机号
     * @param start  从第几条开始查
     * @param limit  查几条
     * @return
     */
    Page<EmptyMobile> findPage(int start, int limit, String mobile);

    /**
     * 根据id查询空号
     *
     * @param id id
     * @return
     */
    EmptyMobile findbyId(Long id);

    /**
     * 修改
     *
     * @param emptyMobile
     * @return
     */
    void update(EmptyMobile emptyMobile);

    /**
     * 删除
     *
     * @param emptyMobile
     * @return
     */
    void delete(EmptyMobile emptyMobile);

    /**
     * 批量保存
     *
     * @param mobileList
     * @return
     */
    void saveBatch(List<EmptyMobile> mobileList);

    /**
     * 根据手机号查询
     *
     * @param mobile
     * @return
     */
    EmptyMobile findByMobile(String mobile);
}