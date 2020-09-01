package cn.emay.core.sms.dao.impl;

import cn.emay.configuration.db.BasePojoSuperDaoImpl;
import cn.emay.core.sms.dao.SmsAppDao;
import cn.emay.core.sms.dto.AppDto;
import cn.emay.core.sms.pojo.SmsApp;
import cn.emay.utils.db.common.Page;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * cn.emay.common.pojo.client.App Dao implement
 *
 * @author frank
 */
@Repository
public class SmsAppDaoImpl extends BasePojoSuperDaoImpl<SmsApp> implements SmsAppDao {

    @Override
    public SmsApp findByAppKey(String appKey) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("appKey", appKey.trim());
        return this.findByProperties(properties);
    }

    @Override
    public Page<AppDto> findPage(int start, int limit, String clientName, String appName, String appKey, String appCode, Integer state) {
        String sql = "select a.*,c.client_name from client c ,sms_app a where a.client_id=c.id and a.app_type='sms'  ";
        List<Object> parameters = new ArrayList<Object>();
        if (!StringUtils.isEmpty(clientName)) {
            sql += " and c.client_name like ? ";
            parameters.add("%" + clientName.trim() + "%");
        }
        if (!StringUtils.isEmpty(appName)) {
            sql += " and a.app_name like ? ";
            parameters.add("%" + appName.trim() + "%");
        }
        if (!StringUtils.isEmpty(appKey)) {
            sql += " and a.app_key = ? ";
            parameters.add(appKey.trim());
        }
        if (!StringUtils.isEmpty(appCode)) {
            sql += " and a.app_code = ? ";
            parameters.add(appCode.trim());
        }
        if (state != null && state > -1) {
            sql += " and a.state = ? ";
            parameters.add(state);
        }
        sql += " order by a.id desc ";
        return findObjectPageByClassInMysql(AppDto.class, sql, start, limit, parameters.toArray());
    }

    @Override
    public AppDto findAppDtoByAppId(Long appId) {
        String sql = "select a.*,c.client_name from client c ,sms_app a where a.client_id=c.id and a.id =?  ";
        sql += " order by a.id desc ";
        List<AppDto> list = findObjectListByClass(AppDto.class, sql, appId);
        if (list.size() > 0) {
            return list.get(0);
        }
        return new AppDto();
    }

    @Override
    public SmsApp findByAppCode(String appCode) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("appCode", appCode.trim());
        return this.findByProperties(properties);
    }

    @Override
    public Page<AppDto> findClientPage(int start, int limit, Long id, String appKey, String appCode, Integer state) {
        String sql = "select a.*,c.client_name from client c ,sms_app a where a.client_id=c.id and a.app_type='sms'  and c.id = ? ";
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(id);
        if (!StringUtils.isEmpty(appKey)) {
            sql += " and a.app_key = ? ";
            parameters.add(appKey.trim());
        }
        if (!StringUtils.isEmpty(appCode)) {
            sql += " and a.app_code = ? ";
            parameters.add(appCode.trim());
        }
        if (state != null && state > -1) {
            sql += " and a.state = ? ";
            parameters.add(state);
        }
        sql += " order by a.id desc ";
        return findObjectPageByClassInMysql(AppDto.class, sql, start, limit, parameters.toArray());
    }

    @Override
    public List<AppDto> findAllAppList(Long id) {
        String sql = "select a.*,c.client_name from client c ,sms_app a where a.client_id=c.id and a.app_type='sms' and a.state=1 and c.id = ? ";
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(id);
        sql += " order by a.id desc ";
        return findObjectListByClass(AppDto.class, sql, parameters.toArray());
    }

    @Override
    public List<SmsApp> findByIds(Long[] appIds) {
        String hql = "from SmsApp where id in ( :ids )  ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ids", appIds);
        return this.getListResult(SmsApp.class, hql, params);
    }


}