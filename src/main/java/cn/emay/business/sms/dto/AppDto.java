package cn.emay.business.sms.dto;

import java.math.BigDecimal;

/**
 * 应用
 *
 * @author chang
 */
public class AppDto implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 启用
     */
    public final static int STATE_ON = 1;
    /**
     * 停用
     */
    public final static int STATE_OFF = 0;

    /**
     * 服务号自选
     */
    public final static int IS_OPTIONAL = 1;
    /**
     * 服务号非自选
     */
    public final static int NOT_OPTIONAL = 0;
    /**
     * APP类型-短信
     */
    public final static String APP_TYPE_SMS = "SMS";

    public AppDto() {
    }

    private Long id;
    /**
     * 所属客户ID
     */
    private Long clientId;
    /**
     * 所属客户
     */
    private String clientName;
    /**
     * 应用名字
     */
    private String appName;
    /**
     * 应用类型(SMS-短信)
     */
    private String appType;
    /**
     * 服务号
     */
    private String appCode;
    /**
     * 应用标识
     */
    private String appKey;
    /**
     * 应用标识
     */
    private String appSecret;
    /**
     * 应用单价
     */
    private BigDecimal price;
    /**
     * 应用状态(0-停用，1-启用)
     */
    private Integer state;

    /**
     * 备注
     */
    private String remark;

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

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
