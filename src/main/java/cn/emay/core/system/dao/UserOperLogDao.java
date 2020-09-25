package cn.emay.core.system.dao;

import cn.emay.core.system.pojo.UserOperLog;
import cn.emay.orm.BaseSuperDao;
import cn.emay.utils.db.common.Page;

import java.util.Date;

/**
 * @author Frank
 */
public interface UserOperLogDao extends BaseSuperDao<UserOperLog> {

    /**
     * 分页查询
     *
     * @param username  用户名
     * @param realname  姓名
     * @param content   日志内容
     * @param startDate 日志记录起始时间
     * @param endDate   日志记录结束时间
     * @param start     从第几条开始查
     * @param limit     查几条
     * @return 分页数据
     */
    Page<UserOperLog> findByPage(String username, String realname, String content, Date startDate, Date endDate, int start, int limit);

}