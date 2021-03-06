package cn.emay.core.base.service.impl;

import cn.emay.core.base.dao.SectionNumberDao;
import cn.emay.core.base.pojo.SectionNumber;
import cn.emay.core.base.service.SectionNumberService;
import cn.emay.utils.db.common.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * cn.emay.common.pojo.base.SectionMobile Service implement
 *
 * @author frank
 */
@Service
public class SectionNumberServiceImpl implements SectionNumberService {

    @Resource
    private SectionNumberDao sectionNumberDao;

    @Override
    public Page<SectionNumber> findPage(int start, int limit, String number, String operatorCode, String provinceCode) {
        return sectionNumberDao.findPage(start, limit, number, operatorCode, provinceCode);
    }

    @Override
    public void save(SectionNumber sectionNumber) {
        List<SectionNumber> sectionNumbers = new ArrayList<>();
        sectionNumbers.add(sectionNumber);
        sectionNumberDao.saveByAutoNamed(sectionNumbers);

    }

    @Override
    public void update(SectionNumber sectionNumber) {
        sectionNumberDao.update(sectionNumber);
    }

    @Override
    public void delete(SectionNumber sectionNumber) {
        List<Long> ids = new ArrayList<>();
        ids.add(sectionNumber.getId());
        sectionNumberDao.updateIsdelete(ids);
    }

    @Override
    public SectionNumber findbyId(Long id) {
        return sectionNumberDao.findById(id);
    }

    @Override
    public void saveBatch(List<SectionNumber> sectionNumbers) {
        sectionNumberDao.saveByAutoNamed(sectionNumbers);
    }

    @Override
    public SectionNumber findByNumber(String number) {
        return sectionNumberDao.findByNumber(number);
    }
}