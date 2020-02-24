package cn.emay.business.sms.dao;

import java.util.List;

import cn.emay.business.sms.dto.AppDto;
import cn.emay.business.sms.pojo.SmsApp;
import cn.emay.orm.BaseSuperDao;
import cn.emay.utils.db.common.Page;

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