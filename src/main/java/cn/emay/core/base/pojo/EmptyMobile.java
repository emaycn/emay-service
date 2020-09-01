package cn.emay.core.base.pojo;

import javax.persistence.*;

/**
 * 空号
 *
 * @author Frank
 */
@Entity
@Table(name = "base_mobile_empty")
public class EmptyMobile implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 手机号码
     */
    @Column(name = "mobile")
    private String mobile;
    /**
     * 是否删除 0:未删除,1:已删除
     */
    @Column(name = "is_delete")
    private Boolean isDelete;

    public EmptyMobile() {
    }

    public EmptyMobile(String mobile, Boolean isDelete) {
        this.mobile = mobile;
        this.isDelete = isDelete;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

}
