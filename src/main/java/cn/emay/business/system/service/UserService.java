package cn.emay.business.system.service;

import java.util.List;

import cn.emay.business.system.dto.UserItemDTO;
import cn.emay.business.system.pojo.User;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.Result;

/**
 * @author Frank
 */
public interface UserService {

    /**
     * 分页查询用户
     *
     * @param start     其实数据位置
     * @param limit     查询的数据条数
     * @param username  用户名
     * @param realname  用户姓名
     * @param mobile    手机号
     * @param userState 用户状态
     * @param userFor
     * @return
     */
    Page<UserItemDTO> findPage(int start, int limit, String username, String realname, String mobile, Integer userState,
                               String userFor);

    /**
     * 保存用户及角色
     */
    /**
     * 新增用户<br/>
     * 保存用户、角色关联、部门关联
     *
     * @param username     用户名
     * @param realname     姓名
     * @param password     密码
     * @param email        邮箱
     * @param mobile       手机号
     * @param roleIds      角色Ids
     * @param departmentId 部门Id
     * @param clientId
     * @param operator     创建人
     * @param userFor      系统
     * @return
     */
    Result add(String username, String realname, String password, String email, String mobile, String roleIds,
               Long departmentId, Long clientId, User operator, String userFor);

    /**
     * 按照ID查找用户
     *
     * @param userId 用户ID
     * @return
     */
    User findById(Long userId);

    /**
     * 停用用户
     *
     * @param userId 用户ID
     * @return
     */
    Result off(Long userId);

    /**
     * 启用用户
     *
     * @param userId 用户ID
     * @return
     */
    Result on(Long userId);

    /**
     * 删除用户
     *
     * @param userId  用户ID
     * @param userFor 归属系统
     * @return
     */
    Result delete(Long userId, String userFor);

    /**
     * 重置用户密码
     *
     * @param userId      用户ID
     * @param newPassword 新密码
     * @return
     */
    Result resetPassword(Long userId, String newPassword);

    /**
     * 更新用户以及相关的角色、部门、客户关联
     *
     * @param realname
     * @param email
     * @param mobile
     * @param roles
     * @param userId
     * @param departmentId
     * @param operType
     * @return
     */
    Result modify(String realname, String email, String mobile, String roles, Long userId, Long departmentId,
                  String operType);

    /**
     * 修改用户密码
     *
     * @param userId      用户ID
     * @param newPassword 新密码
     * @return
     */
    Result modifyPassword(Long userId, String newPassword);

    /**
     * 按照用户名查找用户
     *
     * @param username 用户名
     * @return
     */
    User findByUserName(String username);

    /**
     * 按照部门及同一条件分页查询
     *
     * @param variableName 手机/用户名/姓名 综合查询条件
     * @param departmentId 部门ID
     * @param start        起始
     * @param limit        条数
     * @return
     */
    Page<UserItemDTO> findBycondition(String variableName, Long departmentId, int start, int limit);

    /**
     * 查询所有用户
     *
     * @return
     */
    List<User> findAll();

    List<User> findByIds(List<Long> userIds);

    List<Long> findIdsByRealName(String realName);

    List<Long> findIdsByRealNameAndUserType(String realName, Integer userType);

    Page<UserItemDTO> findClientPage(int start, int limit, String username, String realname, String mobile, Integer userState, Long id);

    List<User> findUsersByClientId(Long clientId);
}
