package cn.emay.core.sms.dao;

import cn.emay.core.sms.dto.AppDto;
import cn.emay.core.sms.pojo.SmsApp;
import cn.emay.orm.BaseSuperDao;
import cn.emay.utils.db.common.Page;

import java.util.List;

/**
 * cn.emay.common.pojo.client.App Dao super
 *
 * @author frank
 */
public interface SmsAppDao extends BaseSuperDao<SmsApp> {

    SmsApp findByAppKey(String appKey);

    Page<AppDto> findPage(int start, int limit, String clientName, String appName, String appKey, String appCode, Integer state);

    AppDto findAppDtoByAppId(Long appId);

    SmsApp findByAppCode(String appCode);

    Page<AppDto> findClientPage(int start, int limit, Long id, String appKey, String appCode, Integer state);

    List<AppDto> findAllAppList(Long id);

    List<SmsApp> findByIds(Long[] appIds);
}