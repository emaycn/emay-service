package cn.emay.core.sms.service;

import cn.emay.core.sms.dto.AppDto;
import cn.emay.core.sms.pojo.SmsApp;
import cn.emay.utils.db.common.Page;

import java.util.List;

/**
 * cn.emay.common.pojo.client.App Service Super
 *
 * @author frank
 */
public interface SmsAppService {

    Page<AppDto> findPage(int start, int limit, String clientName, String appName, String appKey, String appCode, Integer state);

    SmsApp findbyId(Long id);

    void save(SmsApp app);

    SmsApp findByAppKey(String appKey);

    SmsApp findByAppId(Long appId);

    void update(SmsApp app);

    AppDto findAppDtoByAppId(Long appId);

    SmsApp findByAppCode(String appCode);

    Page<AppDto> findClientPage(int start, int limit, Long id, String appKey, String appCode, Integer state);

    List<AppDto> findAllAppList(Long id);

    List<SmsApp> findByIds(Long[] appIds);
}