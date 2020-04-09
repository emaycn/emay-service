package cn.emay.business.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.emay.business.base.dao.EmptyMobileDao;
import cn.emay.business.base.pojo.EmptyMobile;
import cn.emay.business.base.service.EmptyMobileService;
import cn.emay.utils.db.common.Page;

/**
 * cn.emay.common.pojo.base.EmptyMobile Service implement
 *
 * @author frank
 */
@Service
public class EmptyMobileServiceImpl implements EmptyMobileService {

	@Autowired
    private EmptyMobileDao emptyMobileDao;

    @Override
    public Page<EmptyMobile> findPage(int start, int limit, String mobile) {
        return emptyMobileDao.findPage(start, limit, mobile);
    }


    @Override
    public EmptyMobile findbyId(Long id) {
        return emptyMobileDao.findById(id);
    }

    @Override
    public void update(EmptyMobile emptyMobile) {
        emptyMobileDao.update(emptyMobile);
    }

    @Override
    public void delete(EmptyMobile emptyMobile) {
        List<Long> ids = new ArrayList<>();
        ids.add(emptyMobile.getId());
        emptyMobileDao.updateIsdelete(ids);
    }

    @Override
    public void saveBatch(List<EmptyMobile> mobileList) {
        emptyMobileDao.saveByAutoNamed(mobileList);
    }

    @Override
    public EmptyMobile findByMobile(String mobile) {
        return emptyMobileDao.findByMobile(mobile);
    }
}