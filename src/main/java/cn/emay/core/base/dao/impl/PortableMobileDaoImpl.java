package cn.emay.core.base.dao.impl;

import cn.emay.configuration.db.BasePojoSuperDaoImpl;
import cn.emay.core.base.dao.PortableMobileDao;
import cn.emay.core.base.pojo.PortableMobile;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.string.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * cn.emay.common.pojo.base.PortableMobile Dao implement
 *
 * @author frank
 */
@Repository
public class PortableMobileDaoImpl extends BasePojoSuperDaoImpl<PortableMobile> implements PortableMobileDao {

    @Override
    public Page<PortableMobile> findPage(int start, int limit, String mobile) {
        String hql = "from PortableMobile where 1=1 and isDelete=0 ";
        Map<String, Object> params = new HashMap<String, Object>();
        if (!StringUtils.isEmpty(mobile)) {
            hql += " and mobile = :mobile ";
            params.put("mobile", mobile.trim());
        }
        hql += " order by id desc ";
        return this.getPageResult(hql, start, limit, params, PortableMobile.class);
    }

    @Override
    public PortableMobile findByMobile(String mobile) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("mobile", mobile.trim());
        return this.findByProperties(properties);
    }

    @Override
    public void updateIsdelete(List<Long> ids) {
        this.updateIsDelete("base_mobile_portable", ids);
    }

    @Override
    public void saveByAutoNamed(List<PortableMobile> portableMobiles) {
        this.saveByAutoNamed("base_mobile_portable", portableMobiles, true, true);
    }
}