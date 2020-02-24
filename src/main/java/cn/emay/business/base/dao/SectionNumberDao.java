package cn.emay.business.base.dao;

import java.util.List;

import cn.emay.business.base.pojo.SectionNumber;
import cn.emay.orm.BaseSuperDao;
import cn.emay.utils.db.common.Page;

/**
 * cn.emay.common.pojo.base.SectionMobile Dao super
 *
 * @author frank
 */
public interface SectionNumberDao extends BaseSuperDao<SectionNumber> {
    /**
     * 分页查询
     *
     * @param number       号段
     * @param start        从第几条开始查
     * @param limit        查几条
     * @param operatorCode 运营商编码
     * @param provinceCode 省份编码
     * @return
     */
    Page<SectionNumber> findPage(int start, int limit, String number, String operatorCode, String provinceCode);

    SectionNumber findByNumber(String number);

    void updateIsdelete(List<Long> ids);

    void saveByAutoNamed(List<SectionNumber> sectionNumbers);
}