package cn.emay.core.base.dao;

import cn.emay.core.base.pojo.SectionNumber;
import cn.emay.orm.BaseSuperDao;
import cn.emay.utils.db.common.Page;

import java.util.List;

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
     * @return 分页数据
     */
    Page<SectionNumber> findPage(int start, int limit, String number, String operatorCode, String provinceCode);

    SectionNumber findByNumber(String number);

    void updateIsdelete(List<Long> ids);

    void saveByAutoNamed(List<SectionNumber> sectionNumbers);
}