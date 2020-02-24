package cn.emay.business.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.base.dao.BasePojoSuperDaoImpl;
import cn.emay.business.base.dao.BaseSectionNumberDao;
import cn.emay.business.base.pojo.BaseSectionNumber;
import cn.emay.business.base.service.BaseSectionNumberService;
import cn.emay.utils.db.common.Page;

/**
 * cn.emay.common.pojo.base.BaseSectionNumber Service implement
 * 
 * @author frank
 */
@Service
public class BaseSectionNumberServiceImpl extends BasePojoSuperDaoImpl<BaseSectionNumber> implements BaseSectionNumberService {

	@Resource
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