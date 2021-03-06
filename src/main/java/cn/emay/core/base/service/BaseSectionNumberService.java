package cn.emay.core.base.service;


import cn.emay.core.base.pojo.BaseSectionNumber;
import cn.emay.utils.db.common.Page;

import java.util.List;

/**
 * cn.emay.common.pojo.base.SectionBaseMobile Service Super
 *
 * @author frank
 */
public interface BaseSectionNumberService {
    /**
     * 分页查询
     *
     * @param number       号段
     * @param start        从第几条开始查
     * @param limit        查几条
     * @param operatorCode 运营商编码
     * @return 分页数据
     */
    Page<BaseSectionNumber> findPage(int start, int limit, String number, String operatorCode);

    /**
     * 新增号段
     *
     * @param mobileList 号段
     */
    void saveBatchByAutoNamed(List<BaseSectionNumber> mobileList);

    /**
     * 根据id查号段
     *
     * @param id id
     */
    BaseSectionNumber findbyId(Long id);

    /**
     * 更新号段
     *
     * @param baseSectionNumber 号段
     */
    void update(BaseSectionNumber baseSectionNumber);

    /**
     * 删除号段
     *
     * @param baseSectionNumber 号段
     */
    void delete(BaseSectionNumber baseSectionNumber);

}