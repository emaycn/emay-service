package cn.emay.core.system.pojo;

import javax.persistence.*;

/**
 * 用户角色绑定关系
 *
 * @author Frank
 */
@Entity
@Table(name = "system_user_role_assign")
public class UserRoleAssign implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;
    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private Long roleId;

    public UserRoleAssign() {

    }

    public UserRoleAssign(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

}
