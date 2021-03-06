package cn.emay.core.system.dao;

import cn.emay.core.system.pojo.User;
import cn.emay.orm.BaseSuperDao;
import cn.emay.utils.db.common.Page;

import java.util.List;

/**
 * @author Frank
 */
public interface UserDao extends BaseSuperDao<User> {

    /**
     * 分页查询用户
     *
     * @param start     其实数据位置
     * @param limit     查询的数据条数
     * @param username  用户名
     * @param realname  用户姓名
     * @param mobile    手机号
     * @param userState 用户状态
     * @param userFor   所属系统
     * @return 分页数据
     */
    Page<User> findPage(int start, int limit, String username, String realname, String mobile, Integer userState, String userFor);

    /**
     * 用户名是否重复
     *
     * @param username 用户名
     * @return 是否从父
     */
    Boolean hasSameUserName(String username);

    /**
     * 更新状态
     *
     * @param userId 用户ID
     * @param state  用户状态
     */
    void updateState(Long userId, int state);

    /**
     * 按照用户名查找用户
     *
     * @param username 用户名
     * @return 用户
     */
    User findByUserName(String username);

    /**
     * 按照部门及同一条件分页查询
     *
     * @param variableName 手机/用户名/姓名 综合查询条件
     * @param departmentId 部门ID
     * @param start        起始
     * @param limit        条数
     * @return 用户
     */
    Page<User> findBycondition(String variableName, Long departmentId, int start, int limit);

    List<User> findByIds(List<Long> userIds);

    List<Long> findIdsByRealName(String realName);

    List<Long> findIdsByRealNameAndUserType(String realName, Integer userType);

    Page<User> findClientPage(int start, int limit, String username, String realname, String mobile, Integer userState, Long clintId);

    List<User> findUsersByClientId(Long clientId);
}
