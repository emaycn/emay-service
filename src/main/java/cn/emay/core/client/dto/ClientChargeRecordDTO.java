package cn.emay.core.client.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户充值记录
 *
 * @author chang
 */
public class ClientChargeRecordDTO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    //类型：1-充值。2-扣费
    public static final int CHARGE_TYPE_RECHARGE = 1;
    public static final int CHARGE_TYPE_CHARGE = 2;

    private Long id;
    /**
     * 所属客户ID
     */
    private Long clientId;
    /**
     * 所属客户名称
     */
    private String clientName;
    /**
     * 类型
     */
    private Integer chargeType;
    /**
     * 计费
     */
    private BigDecimal charge;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 操作人ID
     */
    private Long chargeUserId;
    /**
     * 操作人名字
     */
    private String chargeUserName;
    /**
     * 充值扣费备注
     */
    private String remark;

    public ClientChargeRecordDTO() {
    }

    public ClientChargeRecordDTO(Long id, Long clientId, String clientName, Integer chargeType, BigDecimal charge, Date createTime, Long chargeUserId, String chargeUserName, String remark) {
        this.id = id;
        this.clientId = clientId;
        this.clientName = clientName;
        this.chargeType = chargeType;
        this.charge = charge;
        this.createTime = createTime;
        this.chargeUserId = chargeUserId;
        this.chargeUserName = chargeUserName;
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getChargeUserName() {
        return chargeUserName;
    }

    public void setChargeUserName(String chargeUserName) {
        this.chargeUserName = chargeUserName;
    }
}
