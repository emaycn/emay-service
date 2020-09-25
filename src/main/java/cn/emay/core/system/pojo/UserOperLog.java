package cn.emay.core.system.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户操作日志
 *
 * @author chang
 */
@Entity
@Table(name = "system_user_oper_log")
public class UserOperLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * ID
     */
    @Column(name = "user_id")
    private Long userId;
    /**
     * 用户名
     */
    @Column(name = "username")
    private String username;
    /**
     * 真实姓名
     */
    @Column(name = "realname")
    private String realname;
    /**
     * 操作模块
     */
    @Column(name = "module")
    private String module;
    /**
     * 内容
     */
    @Column(name = "content")
    private String content;
    /**
     * 操作时间
     */
    @Column(name = "oper_time")
    private Date operTime;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Date getOperTime() {
        return operTime;
    }

    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

}
