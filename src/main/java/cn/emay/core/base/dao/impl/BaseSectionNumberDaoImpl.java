package cn.emay.core.base.dao.impl;

import cn.emay.configuration.db.BasePojoSuperDaoImpl;
import cn.emay.core.base.dao.BaseSectionNumberDao;
import cn.emay.core.base.pojo.BaseSectionNumber;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.string.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * cn.emay.common.pojo.base.SectionBaseMobile Dao implement
 *
 * @author frank
 */
@Repository
public class BaseSectionNumberDaoImpl extends BasePojoSuperDaoImpl<BaseSectionNumber> implements BaseSectionNumberDao {

    @Override
    public Page<BaseSectionNumber> findPage(int start, int limit, String number, String operatorCode) {
        String hql = "from BaseSectionNumber where 1=1 and isDelete=0 ";
        Map<String, Object> params = new HashMap<String, Object>();
        if (!StringUtils.isEmpty(number)) {
            hql += " and number = :number ";
            params.put("number", number.trim());
        }
        if (!StringUtils.isEmpty(operatorCode)) {
            hql += " and operatorCode = :operatorCode ";
            params.put("operatorCode", operatorCode.trim());
        }
        hql += " order by id desc ";
        return this.getPageResult(hql, start, limit, params, BaseSectionNumber.class);
    }

    @Override
    public void updateIsdelete(List<Long> ids) {
        this.updateIsDelete("base_section_number_base", ids);
    }

    @Override
    public void saveByAutoNamed(List<BaseSectionNumber> mobileList) {
        this.saveByAutoNamed("base_section_number_base", mobileList, true, true);
    }
}