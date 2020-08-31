package cn.emay.core.base.pojo;

import javax.persistence.*;

/**
 * 运营商基础号段
 *
 * @author chang
 */
@Entity
@Table(name = "base_section_number")
public class SectionNumber implements java.io.Serializable {

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
     * 城市
     */
    @Column(name = "city")
    private String city;

    /**
     * 运营商CODE[CM-移动，CU-联通，CT-电信]
     */
    @Column(name = "operator_code")
    private String operatorCode;

    /**
     * 省份编码
     */
    @Column(name = "province_code")
    private String provinceCode;

    /**
     * 省份名称
     */
    @Column(name = "province_name")
    private String provinceName;
    /**
     * 是否删除 0:未删除,1:已删除
     */
    @Column(name = "is_delete")
    private Boolean isDelete;

    public SectionNumber() {
    }

    public SectionNumber(String number, String city, String operatorCode, String provinceCode, String provinceName, Boolean isDelete) {
        this.number = number;
        this.city = city;
        this.operatorCode = operatorCode;
        this.provinceCode = provinceCode;
        this.provinceName = provinceName;
        this.isDelete = isDelete;
    }


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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

}
