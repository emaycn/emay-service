package cn.emay.core.system.service.impl;

import cn.emay.constant.web.SystemType;
import cn.emay.core.client.dao.ClientDao;
import cn.emay.core.client.dao.ClientUserDao;
import cn.emay.core.client.pojo.Client;
import cn.emay.core.client.pojo.ClientUser;
import cn.emay.core.system.dao.*;
import cn.emay.core.system.dto.UserItemDTO;
import cn.emay.core.system.pojo.*;
import cn.emay.core.system.service.UserService;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Frank
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;
    @Resource
    private UserRoleAssignDao userRoleAssignDao;
    @Resource
    private DepartmentDao departmentDao;
    @Resource
    private ClientDao clientDao;
    @Resource
    private UserDepartmentAssignDao userDepartmentAssignDao;
    @Resource
    private RoleDao roleDao;
    @Resource
    private ClientUserDao clientUserDao;

    @Override
    public Page<UserItemDTO> findPage(int start, int limit, String username, String realname, String mobile,
                                      Integer userState, String userFor) {
        Page<User> page = userDao.findPage(start, limit, username, realname, mobile, userState, userFor);
        Page<UserItemDTO> result = Page.createByStartAndLimit(start, limit, page.getTotalCount(), new ArrayList<>());
        if (result.getTotalCount() == 0) {
            return result;
        }
        page.getList().forEach(user -> result.getList().add(new UserItemDTO(user)));
        Set<Long> userIds = new HashSet<>();
        page.getList().forEach(user -> userIds.add(user.getId()));
        if (SystemType.OPER.getType().equalsIgnoreCase(userFor)) {
            Map<Long, String> depNameByUserIds = departmentDao.findDepartmentNameByUserIds(userIds);
            result.getList().forEach(userItem -> userItem.setDepartment(depNameByUserIds.get(userItem.getId())));
        }
        if (SystemType.CLIENT.getType().equalsIgnoreCase(userFor)) {
            Map<Long, String> clientNameByUserIds = clientDao.findClientNameByUserIds(userIds);
            result.getList().forEach(userItem -> userItem.setClientName(clientNameByUserIds.get(userItem.getId())));
        }
        Map<Long, String> roleNameByUserIds = roleDao.findRoleNameByUserIds(userIds);
        result.getList().forEach(userItem -> userItem.setRolename(roleNameByUserIds.get(userItem.getId())));
        return result;
    }

    @Override
    public Result add(String username, String realname, String password, String email, String mobile, String roleIds,
                      Long departmentId, Long clientId, User operator, String userFor) {
        List<UserRoleAssign> urs = this.genUserRoles(roleIds);
        if (roleIds.isEmpty()) {
            return Result.badResult("角色不存在");
        }
        if (urs.isEmpty()) {
            return Result.badResult("角色权限不能为空");
        }
        if (userDao.hasSameUserName(username)) {
            return Result.badResult("用户名已存在");
        }
        if (SystemType.OPER.getType().equalsIgnoreCase(userFor)) {
            if (departmentId == null) {
                return Result.badResult("部门不存在");
            }
            Department department = departmentDao.findById(departmentId);
            if (department == null) {
                return Result.badResult("部门不存在");
            }
        }
        if (SystemType.CLIENT.getType().equalsIgnoreCase(userFor)) {
            if (clientId == null) {
                return Result.badResult("客戶不存在");
            }
            Client client = clientDao.findById(clientId);
            if (client == null) {
                return Result.badResult("客戶不存在");
            }
        }
        User user = new User(username, password, realname, mobile, email, operator.getId(), userFor);
        userDao.save(user);
        urs.forEach(ur -> ur.setUserId(user.getId()));
        userRoleAssignDao.saveBatch(urs);
        // 判斷客戶端、運營端，客戶端需要增加客戶用戶關聯表
        if (SystemType.OPER.getType().equalsIgnoreCase(userFor)) {
            UserDepartmentAssign userDepartmentAssign = new UserDepartmentAssign(user.getId(), departmentId);
            userDepartmentAssignDao.save(userDepartmentAssign);
        }
        if (SystemType.CLIENT.getType().equalsIgnoreCase(userFor)) {
            ClientUser clientUser = new ClientUser(clientId, user.getId());
            clientUserDao.save(clientUser);
        }
        return Result.rightResult();
    }

    /**
     * 生成角色用户关联对象
     *
     * @param roleIds 角色Id集合
     */
    private List<UserRoleAssign> genUserRoles(String roleIds) {
        List<UserRoleAssign> urs = new ArrayList<>();
        String[] roleIdArray = roleIds.split(",");
        Arrays.asList(roleIdArray).forEach(id -> urs.add(new UserRoleAssign(null, Long.valueOf(id))));
        List<Role> roles = roleDao.findAll();
        for (Role role : roles) {
            for (UserRoleAssign ur : urs) {
                if (role.getId().longValue() == ur.getRoleId().longValue()) {
                    return urs;
                }
            }
        }
        urs.clear();
        return urs;
    }

    @Override
    public User findById(Long userId) {
        return userDao.findById(userId);
    }

    @Override
    public Result off(Long userId) {
        userDao.updateState(userId, User.STATE_OFF);
        return Result.rightResult();
    }

    @Override
    public Result on(Long userId) {
        userDao.updateState(userId, User.STATE_ON);
        return Result.rightResult();
    }

    @Override
    public Result resetPassword(Long userId, String newPassword) {
        User user = userDao.findById(userId);
        if (user == null) {
            return Result.badResult("用户不存在");
        }
        user.setPassword(newPassword);
        user.setLastChangePasswordTime(null);
        userDao.update(user);
        return Result.rightResult();
    }

    @Override
    public Result delete(Long userId, String userFor) {
        userDao.deleteById(userId);
        userRoleAssignDao.deleteByUserId(userId);
        // 判斷客戶端、運營端，客戶端需要增加客戶用戶關聯表
        if (SystemType.OPER.getType().equalsIgnoreCase(userFor)) {
            userDepartmentAssignDao.deleteByUserId(userId);
        }
        if (SystemType.CLIENT.getType().equalsIgnoreCase(userFor)) {
            clientUserDao.deleteByUserId(userId);
        }
        return Result.rightResult();
    }

    @Override
    public Result modifyPassword(Long userId, String newPassword) {
        User user = userDao.findById(userId);
        if (user == null) {
            return Result.badResult("用户不存在");
        }
        user.setPassword(newPassword);
        user.setLastChangePasswordTime(new Date());
        userDao.update(user);
        return Result.rightResult();
    }

    @Override
    public Result modify(String realname, String email, String mobile, String roleIds, Long userId, Long departmentId, String operType) {
        User user = userDao.findById(userId);
        if (user == null) {
            return Result.badResult("用户不存在");
        }
        List<UserRoleAssign> urs = this.genUserRoles(roleIds);
        if (urs.size() == 0) {
            return Result.badResult("角色不存在");
        }
        urs.forEach(ur -> ur.setUserId(userId));
        if (SystemType.OPER.getType().equalsIgnoreCase(operType)) {
            if (departmentId == null) {
                return Result.badResult("部门不存在");
            }
            Department department = departmentDao.findById(departmentId);
            if (department == null) {
                return Result.badResult("部门不存在");
            }
            UserDepartmentAssign userDepartmentAssign = new UserDepartmentAssign(userId, departmentId);
            userDepartmentAssignDao.deleteByUserId(userId);
            userDepartmentAssignDao.save(userDepartmentAssign);
        }
        user.setRealname(realname);
        user.setEmail(email);
        user.setMobile(mobile);
        userDao.update(user);
        userRoleAssignDao.deleteByUserId(user.getId());
        userRoleAssignDao.saveBatch(urs);
        return Result.rightResult();
    }

    @Override
    public User findByUserName(String username) {
        return userDao.findByUserName(username);
    }

    @Override
    public Page<UserItemDTO> findBycondition(String variableName, Long departmentId, int start, int limit) {
        Page<User> page = userDao.findBycondition(variableName, departmentId, start, limit);
        Page<UserItemDTO> result = Page.createByStartAndLimit(start, limit, page.getTotalCount(), new ArrayList<>());
        if (result.getTotalCount() == 0) {
            return result;
        }
        page.getList().forEach(user -> result.getList().add(new UserItemDTO(user)));
        Set<Long> userIds = new HashSet<>();
        page.getList().forEach(user -> userIds.add(user.getId()));
        Map<Long, String> roleNameByUserIds = roleDao.findRoleNameByUserIds(userIds);
        result.getList().forEach(userItem -> userItem.setRolename(roleNameByUserIds.get(userItem.getId())));
        return result;
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public List<User> findByIds(List<Long> userIds) {
        return userDao.findByIds(userIds);
    }

    @Override
    public List<Long> findIdsByRealName(String realName) {
        return userDao.findIdsByRealName(realName);
    }

    @Override
    public List<Long> findIdsByRealNameAndUserType(String realName, Integer userType) {
        return userDao.findIdsByRealNameAndUserType(realName, userType);
    }

    @Override
    public Page<UserItemDTO> findClientPage(int start, int limit, String username, String realname, String mobile, Integer userState, Long clintId) {
        Page<User> page = userDao.findClientPage(start, limit, username, realname, mobile, userState, clintId);
        Page<UserItemDTO> result = Page.createByStartAndLimit(start, limit, page.getTotalCount(), new ArrayList<>());
        if (result.getTotalCount() == 0) {
            return result;
        }
        page.getList().forEach(user -> result.getList().add(new UserItemDTO(user)));
        Set<Long> userIds = new HashSet<>();
        page.getList().forEach(user -> userIds.add(user.getId()));
        Map<Long, String> clientNameByUserIds = clientDao.findClientNameByUserIds(userIds);
        result.getList().forEach(userItem -> userItem.setClientName(clientNameByUserIds.get(userItem.getId())));
        Map<Long, String> roleNameByUserIds = roleDao.findRoleNameByUserIds(userIds);
        result.getList().forEach(userItem -> userItem.setRolename(roleNameByUserIds.get(userItem.getId())));
        return result;
    }

    @Override
    public List<User> findUsersByClientId(Long clientId) {
        return userDao.findUsersByClientId(clientId);
    }

}
