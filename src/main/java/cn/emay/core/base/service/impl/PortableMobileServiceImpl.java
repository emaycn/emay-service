package cn.emay.core.base.service.impl;

import cn.emay.core.base.dao.PortableMobileDao;
import cn.emay.core.base.pojo.PortableMobile;
import cn.emay.core.base.service.PortableMobileService;
import cn.emay.utils.db.common.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * cn.emay.common.pojo.base.PortableMobile Service implement
 *
 * @author frank
 */
@Service
public class PortableMobileServiceImpl implements PortableMobileService {

    @Resource
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