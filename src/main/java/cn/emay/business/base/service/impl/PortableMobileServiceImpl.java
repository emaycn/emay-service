package cn.emay.business.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.emay.business.base.dao.PortableMobileDao;
import cn.emay.business.base.pojo.PortableMobile;
import cn.emay.business.base.service.PortableMobileService;
import cn.emay.utils.db.common.Page;

/**
 * cn.emay.common.pojo.base.PortableMobile Service implement
 * 
 * @author frank
 */
@Service
public class PortableMobileServiceImpl implements PortableMobileService {

	@Autowired
	private PortableMobileDao portableMobileDao;

	@Override
	public Page<PortableMobile> findPage(int start, int limit, String mobile) {
		return portableMobileDao.findPage(start, limit, mobile);
	}

	@Override
	public void save(PortableMobile portableMobile) {
		List<PortableMobile> portableMobiles = new ArrayList<>();
		portableMobiles.add(portableMobile);
		portableMobileDao.saveByAutoNamed(portableMobiles);
	}

	@Override
	public PortableMobile findbyId(Long id) {
		return portableMobileDao.findById(id);
	}

	@Override
	public void update(PortableMobile portableMobile) {
		portableMobileDao.update(portableMobile);
	}

	@Override
	public void delete(PortableMobile portableMobile) {
		List<Long> ids = new ArrayList<>();
		ids.add(portableMobile.getId());
		portableMobileDao.updateIsdelete(ids);
	}

	@Override
	public void saveBatch(List<PortableMobile> portableMobiles) {
		portableMobileDao.saveByAutoNamed(portableMobiles);
	}

	@Override
	public PortableMobile findByMobile(String mobile) {
		return portableMobileDao.findByMobile(mobile);
	}
}