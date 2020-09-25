package cn.emay.core.sms.service.impl;

import cn.emay.core.sms.dao.SmsAppDao;
import cn.emay.core.sms.dto.AppDto;
import cn.emay.core.sms.pojo.SmsApp;
import cn.emay.core.sms.service.SmsAppService;
import cn.emay.utils.db.common.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * cn.emay.common.pojo.client.App Service implement
 *
 * @author frank
 */
@Service
public class SmsAppServiceImpl implements SmsAppService {

    @Resource
    private SmsAppDao appDao;

    @Override
    public Page<AppDto> findPage(int start, int limit, String clientName, String appName, String appKey, String appCode, Integer state) {
        return appDao.findPage(start, limit, clientName, appName, appKey, appCode, state);
    }


    @Override
    public SmsApp findbyId(Long id) {
        return appDao.findById(id);
    }

    @Override
    public void save(SmsApp app) {
        appDao.save(app);
    }

    @Override
    public SmsApp findByAppKey(String appKey) {
        return appDao.findByAppKey(appKey);
    }

    @Override
    public SmsApp findByAppId(Long appId) {
        return appDao.findById(appId);
    }

    @Override
    public void update(SmsApp app) {
        appDao.update(app);
    }

    @Override
    public AppDto findAppDtoByAppId(Long appId) {
        return appDao.findAppDtoByAppId(appId);
    }

    @Override
    public SmsApp findByAppCode(String appCode) {
        return appDao.findByAppCode(appCode);
    }

    @Override
    public Page<AppDto> findClientPage(int start, int limit, Long id, String appKey, String appCode, Integer state) {
        return appDao.findClientPage(start, limit, id, appKey, appCode, state);
    }

    @Override
    public List<AppDto> findAllAppList(Long id) {
        return appDao.findAllAppList(id);
    }


    @Override
    public List<SmsApp> findByIds(Long[] appIds) {
        return appDao.findByIds(appIds);
    }
}