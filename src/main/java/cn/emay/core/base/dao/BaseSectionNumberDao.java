package cn.emay.core.base.dao;

import cn.emay.core.base.pojo.BaseSectionNumber;
import cn.emay.orm.BaseSuperDao;
import cn.emay.utils.db.common.Page;

import java.util.List;

/**
 * cn.emay.common.pojo.base.SectionBaseMobile Dao super
 *
 * @author frank
 */
public interface BaseSectionNumberDao extends BaseSuperDao<BaseSectionNumber> {
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

    void updateIsdelete(List<Long> ids);

    void saveByAutoNamed(List<BaseSectionNumber> mobileList);
}