package cn.emay.core.system.service.impl;

import cn.emay.constant.web.OperType;
import cn.emay.core.system.dao.UserOperLogDao;
import cn.emay.core.system.pojo.User;
import cn.emay.core.system.pojo.UserOperLog;
import cn.emay.core.system.service.UserOperLogService;
import cn.emay.utils.WebUtils;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.string.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author chang
 */
@Service
public class UserOperLogServiceImpl implements UserOperLogService {

    @Autowired
    private UserOperLogDao userOperLogDao;

    @Override
    public Page<UserOperLog> findByPage(String username, String realname, String content, Date startDate, Date endDate, int start, int limit) {
        username = !StringUtils.isEmpty(username) ? username.toLowerCase() : username;
        return userOperLogDao.findByPage(username, realname, content, startDate, endDate, start, limit);
    }

    @Override
    public void saveOperLog(String module, String content, OperType type) {
        User user = WebUtils.getCurrentUser();
        if (user == null) {
            throw new IllegalArgumentException("no user login");
        }
        UserOperLog log = new UserOperLog();
        log.setContent(content);
        log.setModule(module);
        log.setUsername(user.getUsername());
        log.setOperTime(new Date());
        log.setOperType(type.getType());
        log.setUserId(user.getId());
        log.setRealname(user.getRealname());
        userOperLogDao.save(log);
    }

}