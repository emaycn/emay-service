package cn.emay.core.system.pojo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 角色部门 绑定关系
 *
 * @author lijunjian
 */
@Entity
@Table(name = "system_user_department_assign")
public class UserDepartmentAssign implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 部门ID
     */
    @Column(name = "system_department_id")
    private Long systemDepartmentId;

    /**
     * 用户ID
     */
    @Column(name = "system_user_id")
    private Long systemUserId;

    public UserDepartmentAssign() {

    }

    public UserDepartmentAssign(Long systemUserId, Long systemDepartmentId) {
        this.systemDepartmentId = systemDepartmentId;
        this.systemUserId = systemUserId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSystemDepartmentId() {
        return systemDepartmentId;
    }

    public void setSystemDepartmentId(Long systemDepartmentId) {
        this.systemDepartmentId = systemDepartmentId;
    }

    public Long getSystemUserId() {
        return systemUserId;
    }

    public void setSystemUserId(Long systemUserId) {
        this.systemUserId = systemUserId;
    }
}
