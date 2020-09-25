package cn.emay.core.system.service.impl;

import cn.emay.core.system.dao.UserRoleAssignDao;
import cn.emay.core.system.pojo.UserRoleAssign;
import cn.emay.core.system.service.UserRoleAssignService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author frank
 */
@Service
public class UserRoleAssignServiceImpl implements UserRoleAssignService {

    @Resource
    private UserRoleAssignDao userRoleAssignDao;

    @Override
    public Boolean findExistsByRoleId(Long roleId) {
        boolean isExists = true;
        List<UserRoleAssign> list = userRoleAssignDao.findByRoleId(roleId);
        if (null == list || list.isEmpty()) {
            isExists = false;
        }
        return isExists;
    }

    @Override
    public List<UserRoleAssign> findByUserId(Long userId) {
        return userRoleAssignDao.findByUserId(userId);
    }

}
