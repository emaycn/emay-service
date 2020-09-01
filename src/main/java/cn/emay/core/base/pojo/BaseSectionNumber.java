package cn.emay.core.base.pojo;

import javax.persistence.*;

/**
 * 运营商号段
 *
 * @author chang
 */
@Entity
@Table(name = "base_section_number_base")
public class BaseSectionNumber implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 号段
     */
    @Column(name = "number")
    private String number;

    /**
     * 运营商CODE[CM-移动，CU-联通，CT-电信]
     */
    @Column(name = "operator_code")
    private String operatorCode;

    /**
     * 是否删除 0:未删除,1:已删除
     */
    @Column(name = "is_delete")
    private Boolean isDelete;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

}
