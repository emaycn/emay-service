package cn.emay.boot.business.system.dao;

import cn.emay.boot.business.system.pojo.UserDepartmentAssign;
import cn.emay.orm.BaseSuperDao;

public interface UserDepartmentAssignDao extends BaseSuperDao<UserDepartmentAssign>{

	Long findByDepId(Long id);
	
	void deleteDataByUserId(Long userId);
	
	UserDepartmentAssign findByUserId(Long userId);
}
