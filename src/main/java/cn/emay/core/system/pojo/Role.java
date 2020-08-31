package cn.emay.core.system.pojo;

import javax.persistence.*;
import java.util.Date;

/**
 * 角色
 *
 * @author Frank
 */
@Entity
@Table(name = "system_role")
public class Role implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 运营系统角色-oper
     */
    public final static String OPER_TYPE_OPER = "OPER";
    /**
     * 客戶系统角色-client
     */
    public final static String OPER_TYPE_CLIENT = "CLIENT";

    /**
     *
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 角色名
     */
    @Column(name = "role_name")
    private String roleName;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
    /**
     * 角色类型【OPER-运营端、CLIENT-客户端】
     */
    @Column(name = "role_type")
    private String roleType;

    public Role() {

    }

    public Role(String roleName, String remark) {
        this.roleName = roleName;
        this.createTime = new Date();
        this.remark = remark;
    }

    public Role(String roleName, String remark, String roleType) {
        this.roleName = roleName;
        this.createTime = new Date();
        this.remark = remark;
        this.roleType = roleType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

}
