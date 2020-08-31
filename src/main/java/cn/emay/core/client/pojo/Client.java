package cn.emay.core.client.pojo;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户
 *
 * @author chang
 */
@Entity
@Table(name = "client")
public class Client implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 名称
     */
    @Column(name = "client_name")
    private String clientName;
    /**
     * 联系人
     */
    @Column(name = "linkman")
    private String linkman;
    /**
     * 手机号
     */
    @Column(name = "mobile")
    private String mobile;
    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;
    /**
     * 地址
     */
    @Column(name = "address")
    private String address;
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    /**
     * 创建人
     */
    @Column(name = "operator_id")
    private Long operatorId;
    /**
     * 资金余额
     */
    @Column(name = "balance")
    private BigDecimal balance;
    /**
     * 是否余额预警
     */
    @Column(name = "is_balance_warning")
    private Boolean isBalanceWarning;

    public Client() {

    }

    public Client(Long id, String clientName, String linkman, String mobile, String email, String address, Date createTime, Long operatorId, BigDecimal balance,
                  Boolean isBalancewarning) {
        super();
        this.id = id;
        this.clientName = clientName;
        this.linkman = linkman;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.createTime = createTime;
        this.operatorId = operatorId;
        this.balance = balance;
        this.setIsBalanceWarning(isBalancewarning);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Boolean getIsBalanceWarning() {
        return isBalanceWarning;
    }

    public void setIsBalanceWarning(Boolean isBalanceWarning) {
        this.isBalanceWarning = isBalanceWarning;
    }

}
