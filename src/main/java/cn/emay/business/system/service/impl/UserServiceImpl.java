package cn.emay.business.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.emay.business.client.dao.ClientDao;
import cn.emay.business.client.dao.ClientUserDao;
import cn.emay.business.client.pojo.Client;
import cn.emay.business.client.pojo.ClientUser;
import cn.emay.business.system.dao.DepartmentDao;
import cn.emay.business.system.dao.RoleDao;
import cn.emay.business.system.dao.UserDao;
import cn.emay.business.system.dao.UserDepartmentAssignDao;
import cn.emay.business.system.dao.UserRoleAssignDao;
import cn.emay.business.system.dto.UserItemDTO;
import cn.emay.business.system.pojo.Department;
import cn.emay.business.system.pojo.Role;
import cn.emay.business.system.pojo.User;
import cn.emay.business.system.pojo.UserDepartmentAssign;
import cn.emay.business.system.pojo.UserRoleAssign;
import cn.emay.business.system.service.UserService;
import cn.emay.utils.db.common.Page;
import cn.emay.utils.result.Result;

/**
 * @author Frank
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRoleAssignDao userRoleAssignDao;
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private UserDepartmentAssignDao userDepartmentAssignDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private ClientUserDao clientUserDao;

    @Override
    public Page<UserItemDTO> findPage(int start, int limit, String username, String realname, String mobile,
                                      Integer userState, String userFor) {
        Page<User> page = userDao.findPage(start, limit, username, realname, mobile, userState, userFor);
        Page<UserItemDTO> result = Page.createByStartAndLimit(start, limit, page.getTotalCount(),
                new ArrayList<UserItemDTO>());
        if (result.getTotalCount() == 0) {
            return result;
        }
        page.getList().stream().forEach(user -> result.getList().add(new UserItemDTO(user)));
        Set<Long> userIds = new HashSet<>();
        page.getList().stream().forEach(user -> userIds.add(user.getId()));
        if (User.USER_FOR_OPER.equalsIgnoreCase(userFor)) {
            Map<Long, String> depNameByUserIds = departmentDao.findDepartmentNameByUserIds(userIds);
            result.getList().stream()
                    .forEach(userItem -> userItem.setDepartment(depNameByUserIds.get(userItem.getId())));
        }
        if (User.USER_FOR_CLIENT.equalsIgnoreCase(userFor)) {
            Map<Long, String> clientNameByUserIds = clientDao.findClientNameByUserIds(userIds);
            result.getList().stream()
                    .forEach(userItem -> userItem.setClientName(clientNameByUserIds.get(userItem.getId())));
        }
        Map<Long, String> roleNameByUserIds = roleDao.findRoleNameByUserIds(userIds);
        result.getList().stream().forEach(userItem -> userItem.setRolename(roleNameByUserIds.get(userItem.getId())));
        return result;
    }

    @Override
    public Result add(String username, String realname, String password, String email, String mobile, String roleIds,
                      Long departmentId, Long clientId, User operator, String userFor) {
        List<UserRoleAssign> urs = this.genUserRoles(roleIds);
        if (urs.size() == 0) {
            return Result.badResult("角色不存在");
        }
        if (urs.size() == 0) {
            return Result.badResult("角色权限不能为空");
        }
        if (userDao.hasSameUserName(username)) {
            return Result.badResult("用户名已存在");
        }
        if (User.USER_FOR_OPER.equalsIgnoreCase(userFor)) {
            if (departmentId == null) {
                return Result.badResult("部门不存在");
            }
            Department department = departmentDao.findById(departmentId);
            if (department == null) {
                return Result.badResult("部门不存在");
            }
        }
        if (User.USER_FOR_CLIENT.equalsIgnoreCase(userFor)) {
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
        urs.stream().forEach(ur -> ur.setUserId(user.getId()));
        userRoleAssignDao.saveBatch(urs);
        // 判斷客戶端、運營端，客戶端需要增加客戶用戶關聯表
        if (User.USER_FOR_OPER.equalsIgnoreCase(userFor)) {
            UserDepartmentAssign userDepartmentAssign = new UserDepartmentAssign(user.getId(), departmentId);
            userDepartmentAssignDao.save(userDepartmentAssign);
        }
        if (User.USER_FOR_CLIENT.equalsIgnoreCase(userFor)) {
            ClientUser clientUser = new ClientUser(clientId, user.getId());
            clientUserDao.save(clientUser);
        }
        return Result.rightResult();
    }

    /**
     * 生成角色用户关联对象
     *
     * @param roleIds 角色Id集合
     * @return
     */
    private List<UserRoleAssign> genUserRoles(String roleIds) {
        List<UserRoleAssign> urs = new ArrayList<UserRoleAssign>();
        String[] roleIdArray = roleIds.split(",");
        Arrays.asList(roleIdArray).stream().forEach(id -> urs.add(new UserRoleAssign(null, Long.valueOf(id))));
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
        if (User.USER_FOR_OPER.equalsIgnoreCase(userFor)) {
            userDepartmentAssignDao.deleteByUserId(userId);
        }
        if (User.USER_FOR_CLIENT.equalsIgnoreCase(userFor)) {
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
        urs.stream().forEach(ur -> ur.setUserId(userId));
        if (User.USER_FOR_OPER.equalsIgnoreCase(operType)) {
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
        Page<UserItemDTO> result = Page.createByStartAndLimit(start, limit, page.getTotalCount(),
                new ArrayList<UserItemDTO>());
        if (result.getTotalCount() == 0) {
            return result;
        }
        page.getList().stream().forEach(user -> result.getList().add(new UserItemDTO(user)));
        Set<Long> userIds = new HashSet<>();
        page.getList().stream().forEach(user -> userIds.add(user.getId()));
        Map<Long, String> roleNameByUserIds = roleDao.findRoleNameByUserIds(userIds);
        result.getList().stream().forEach(userItem -> userItem.setRolename(roleNameByUserIds.get(userItem.getId())));
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
        Page<UserItemDTO> result = Page.createByStartAndLimit(start, limit, page.getTotalCount(),
                new ArrayList<UserItemDTO>());
        if (result.getTotalCount() == 0) {
            return result;
        }
        page.getList().stream().forEach(user -> result.getList().add(new UserItemDTO(user)));
        Set<Long> userIds = new HashSet<>();
        page.getList().stream().forEach(user -> userIds.add(user.getId()));
        Map<Long, String> clientNameByUserIds = clientDao.findClientNameByUserIds(userIds);
        result.getList().stream()
                .forEach(userItem -> userItem.setClientName(clientNameByUserIds.get(userItem.getId())));
        Map<Long, String> roleNameByUserIds = roleDao.findRoleNameByUserIds(userIds);
        result.getList().stream().forEach(userItem -> userItem.setRolename(roleNameByUserIds.get(userItem.getId())));
        return result;
    }

    @Override
    public List<User> findUsersByClientId(Long clientId) {
        return userDao.findUsersByClientId(clientId);
    }

}
