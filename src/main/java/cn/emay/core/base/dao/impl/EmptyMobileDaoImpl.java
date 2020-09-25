package cn.emay.core.base.dao.impl;

import cn.emay.configuration.db.BasePojoSuperDaoImpl;
import cn.emay.core.base.dao.EmptyMobileDao;
import cn.emay.core.base.pojo.EmptyMobile;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.string.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * cn.emay.common.pojo.base.EmptyMobile Dao implement
 *
 * @author frank
 */
@Repository
public class EmptyMobileDaoImpl extends BasePojoSuperDaoImpl<EmptyMobile> implements EmptyMobileDao {

    @Override
    public Page<EmptyMobile> findPage(int start, int limit, String mobile) {
        String hql = "from EmptyMobile where 1=1 and isDelete=0 ";
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(mobile)) {
            hql += " and mobile = :mobile ";
            params.put("mobile", mobile.trim());
        }
        hql += " order by id desc ";
        return this.getPageResult(hql, start, limit, params, EmptyMobile.class);
    }

    @Override
    public EmptyMobile findByMobile(String mobile) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("mobile", mobile.trim());
        return this.findByProperties(properties);
    }

    @Override
    public void updateIsdelete(List<Long> ids) {
        this.updateIsDelete("base_mobile_empty", ids);
    }

    @Override
    public void saveByAutoNamed(List<EmptyMobile> emptyMobiles) {
        this.saveByAutoNamed("base_mobile_empty", emptyMobiles, true, true);
    }

}