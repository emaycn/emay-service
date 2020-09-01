package cn.emay.core.base.service.impl;

import cn.emay.configuration.db.BasePojoSuperDaoImpl;
import cn.emay.core.base.dao.BaseSectionNumberDao;
import cn.emay.core.base.pojo.BaseSectionNumber;
import cn.emay.core.base.service.BaseSectionNumberService;
import cn.emay.utils.db.common.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * cn.emay.common.pojo.base.BaseSectionNumber Service implement
 *
 * @author frank
 */
@Service
public class BaseSectionNumberServiceImpl extends BasePojoSuperDaoImpl<BaseSectionNumber> implements BaseSectionNumberService {

    @Autowired
    private BaseSectionNumberDao baseSectionNumberDao;

    @Override
    public Page<BaseSectionNumber> findPage(int start, int limit, String number, String operatorCode) {
        return baseSectionNumberDao.findPage(start, limit, number, operatorCode);
    }

    @Override
    public void saveBatchByAutoNamed(List<BaseSectionNumber> mobileList) {
        baseSectionNumberDao.saveByAutoNamed(mobileList);
    }

    @Override
    public BaseSectionNumber findbyId(Long id) {
        return baseSectionNumberDao.findById(id);
    }

    @Override
    public void update(BaseSectionNumber baseSectionNumber) {
        baseSectionNumberDao.update(baseSectionNumber);
    }

    @Override
    public void delete(BaseSectionNumber baseSectionNumber) {
        List<Long> ids = new ArrayList<>();
        ids.add(baseSectionNumber.getId());
        baseSectionNumberDao.updateIsdelete(ids);
    }
}