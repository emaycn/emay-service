package cn.emay.boot.business.system.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.emay.boot.base.dao.BaseSuperDaoImpl;
import cn.emay.boot.business.system.dao.DepartmentDao;
import cn.emay.boot.business.system.pojo.Department;
import cn.emay.boot.utils.CheckUtil;
import cn.emay.utils.db.common.Page;

@Repository
public class DepartmentDaoImpl extends BaseSuperDaoImpl<Department> implements DepartmentDao{

	@Override
	public Page<Department> findDepartmentByLikeName(Long id, String departmentName,Long enterpriseId,int start,int limit) {
		Map<String, Object> param = new HashMap<String, Object>();
		String hql = "from Department where isDelete=:isDelete and parentDepartmentId =:id and enterpriseId =:enterpriseId";
		param.put("isDelete", false);
		param.put("id", id);
		param.put("enterpriseId", enterpriseId);
		if (!StringUtils.isEmpty(departmentName)) {
			hql = hql + " and departmentName like:departmentName";
			param.put("departmentName", "%" + CheckUtil.specialCodeEscape(departmentName) + "%");
		}
		Page<Department> page = this.getPageResult(hql, start, limit, param, Department.class);
		return page;
	}
	
	@Override
	public List<Department> findByIds(List<Long> list) {
		Map<String, Object> param = new HashMap<String, Object>();
		String hql = "from Department where isDelete=:isDelete and id in(:ids)";
		param.put("isDelete", false);
		param.put("ids", list);
		return this.getListResult(Department.class, hql, param);
	}
	
	@Override
	public Long findCountByParentId(Long parentId) {
		Map<String, Object> param = new HashMap<String, Object>();
		String hql = "select count(*) from Department where isDelete=:isDelete and parentDepartmentId = :parentId";
		param.put("isDelete", false);
		param.put("parentId", parentId);
		return (Long) super.getUniqueResult(hql, param);
	}

	@Override
	public void deleteDepartment(Long departmentId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql ="update Department set isDelete=1 where id=:id";
		params.put("id", departmentId);
		this.execByHql(hql, params);
	}
	
	@Override
	public List<Department> findByParentId(Long parentId,Long enterpriseId) {
		Map<String, Object> param = new HashMap<String, Object>();
		String hql = "from Department where isDelete=:isDelete and parentDepartmentId = :parentId and enterpriseId =:enterpriseId";
		param.put("isDelete", false);
		param.put("parentId", parentId);
		param.put("enterpriseId", enterpriseId);
		return this.getListResult(Department.class, hql, param);
	}
	
	@Override
	public Long findDepartmentByName(String departmentName, Long id) {
		Map<String, Object> param = new HashMap<String, Object>();
		String hql = "SELECT count(*) from Department where departmentName=:departmentName and isDelete =:isDelete";
		if (id != null) {
			hql = hql + " and id <>:id";
			param.put("id", id);
		}
		param.put("departmentName", departmentName);
		param.put("isDelete", false);
		return (Long) super.getUniqueResult(hql, param);
	}
}