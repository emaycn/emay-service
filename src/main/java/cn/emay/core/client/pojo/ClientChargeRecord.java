package cn.emay.core.client.pojo;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户充值记录
 *
 * @author chang
 */
@Entity
@Table(name = "client_charge_record")
public class ClientChargeRecord implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    //类型：1-充值。2-扣费
    public static final int CHARGE_TYPE_RECHARGE = 1;
    public static final int CHARGE_TYPE_CHARGE = 2;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 所属客户ID
     */
    @Column(name = "client_id")
    private Long clientId;
    /**
     * 类型
     */
    @Column(name = "charge_type")
    private Integer chargeType;
    /**
     * 计费
     */
    @Column(name = "charge")
    private BigDecimal charge;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    /**
     * 操作人人ID
     */
    @Column(name = "charge_user_id")
    private Long chargeUserId;
    /**
     * 充值扣费备注
     */
    @Column(name = "remark")
    private String remark;

    public ClientChargeRecord() {
    }

    public ClientChargeRecord(Long clientId, Integer chargeType, BigDecimal charge, Date createTime, Long chargeUserId, String remark) {
        this.clientId = clientId;
        this.chargeType = chargeType;
        this.charge = charge;
        this.createTime = createTime;
        this.chargeUserId = chargeUserId;
        this.remark = remark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Integer getChargeType() {
        return chargeType;
    }

    public void setChargeType(Integer chargeType) {
        this.chargeType = chargeType;
    }

    public BigDecimal getCharge() {
        return charge;
    }

    public void setCharge(BigDecimal charge) {
        this.charge = charge;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getChargeUserId() {
        return chargeUserId;
    }

    public void setChargeUserId(Long chargeUserId) {
        this.chargeUserId = chargeUserId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
