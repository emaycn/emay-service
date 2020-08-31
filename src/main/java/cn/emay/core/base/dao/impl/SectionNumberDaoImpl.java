package cn.emay.core.base.dao.impl;

import cn.emay.configuration.db.BasePojoSuperDaoImpl;
import cn.emay.core.base.dao.SectionNumberDao;
import cn.emay.core.base.pojo.SectionNumber;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.string.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * cn.emay.common.pojo.base.SectionMobile Dao implement
 *
 * @author frank
 */
@Repository
public class SectionNumberDaoImpl extends BasePojoSuperDaoImpl<SectionNumber> implements SectionNumberDao {

    @Override
    public Page<SectionNumber> findPage(int start, int limit, String number, String operatorCode, String provinceCode) {
        String hql = "from SectionNumber where 1=1 and isDelete=0 ";
        Map<String, Object> params = new HashMap<String, Object>();
        if (!StringUtils.isEmpty(number)) {
            hql += " and number = :number ";
            params.put("number", number.trim());
        }
        if (!StringUtils.isEmpty(operatorCode) && !"0".equals(operatorCode)) {
            hql += " and operatorCode = :operatorCode ";
            params.put("operatorCode", operatorCode.trim());
        }
        if (!StringUtils.isEmpty(provinceCode) && !"00".equals(provinceCode)) {
            hql += " and provinceCode = :provinceCode ";
            params.put("provinceCode", provinceCode.trim());
        }
        hql += " order by id desc ";
        return this.getPageResult(hql, start, limit, params, SectionNumber.class);
    }

    @Override
    public SectionNumber findByNumber(String number) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("number", number.trim());
        return this.findByProperties(properties);
    }

    @Override
    public void updateIsdelete(List<Long> ids) {
        this.updateIsDelete("base_section_number", ids);
    }

    @Override
    public void saveByAutoNamed(List<SectionNumber> sectionNumbers) {
        this.saveByAutoNamed("base_section_number", sectionNumbers, true, true);
    }
}