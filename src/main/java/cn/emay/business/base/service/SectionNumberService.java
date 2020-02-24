package cn.emay.business.base.service;

import java.util.List;

import cn.emay.business.base.pojo.SectionNumber;
import cn.emay.utils.db.common.Page;

/**
 * cn.emay.common.pojo.base.SectionMobile Service Super
 * 
 * @author frank
 */
public interface SectionNumberService {
    /**
     * 分页查询
     *
     * @param number 号段
     * @param start  从第几条开始查
     * @param limit  查几条
     * @param operatorCode 运营商编码
     * @param provinceCode 省份编码
     * @return
     */
    Page<SectionNumber> findPage(int start, int limit, String number, String operatorCode, String provinceCode);

	void save(SectionNumber sectionNumber);

	void update(SectionNumber sectionNumber);

	void delete(SectionNumber sectionNumber);

	SectionNumber findbyId(Long id);

    void saveBatch(List<SectionNumber> sectionNumbers);

    SectionNumber findByNumber(String number);
}