package cn.emay.core.sms.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 短信信息
 *
 * @author Frank
 */
public class SmsMessageOperDto implements Serializable {
    // 待发送
    public static final int STATE_WAIT_SEND = 1;
    // 发送中
    public static final int STATE_SENDING = 2;
    // 已发送
    public static final int STATE_HAS_SEND = 3;
    // 发送成功
    public static final int STATE_SUCCESS = 4;
    // 发送失败
    public static final int STATE_FAIL = 5;

    private static final long serialVersionUID = 1L;

    /**
     * 页面发送类型
     */
    public static final int SEND_TYPE_PAGE = 2;
    /**
     * 接口发送类型
     */
    public static final int SEND_TYPE_INTER = 1;
    /**
     * 系统级别-最高级别
     */
    public static final int PRIORITY_SYSTEM = 3;
    /**
     * 高级别-次高级别
     */
    public static final int PRIORITY_HIG = 2;
    /**
     * 低级别-低级别
     */
    public static final int PRIORITY_LOW = 1;


    /**
     * id 等同于 smsid
     */
    private Long id;
    /**
     * 手机
     */
    private String mobile;
    /**
     * 扩展码
     */
    private String extendedCode;
    /**
     * APP KEY
     */
    private String appKey;
    /**
     * APP 扩展
     */
    private String appCode;
    /**
     * 发送类型：1-接口、2-页面
     */
    private Integer sendType;
    /**
     * 提交时间
     */
    private Date submitTime;
    /**
     * 内容
     */
    private String content;
    /**
     * SMS ID
     */
    private String smsId;
    /**
     * 客户自定义 SMS ID
     */
    private String customId;
    /**
     * 接口编码
     */
    private String interfaceServiceNo;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 客户IP
     */
    private String remoteIp;
    /**
     * 客户名
     */
    private String clientName;
    /**
     * 通道ID
     */
    private Long channelId;
    /**
     * 通道名
     */
    private String channelName;
    /**
     * 运营商CODE
     */
    private String operatorCode;
    /**
     * 省份Code
     */
    private String provinceCode;
    /**
     * 城市
     */
    private String city;
    /**
     * 计费条数
     */
    private Integer cost;
    /**
     * 状态：1-待发送，2-发送中，3-已发送，4-发送成功，5-发送失败，6-发送超时
     */
    private Integer state;
    /**
     * 发送时间
     */
    private Date sendTime;
    /**
     * 状态报告返回时间
     */
    private Date reportTime;
    /**
     * 响应返回时间
     */
    private Date responseTime;
    /**
     * 运营商响应代码
     */
    private String responseCode;
    /**
     * 运营商状态报告代码
     */
    private String reportCode;
    /**
     * 最终的状态代码【自身定义】
     */
    private String resultCode;
    /**
     * 计费
     */
    private BigDecimal charge;
    /**
     * 应用单价
     */
    private BigDecimal price;
    /**
     * 客户ID
     */
    private Long clientId;
    /**
     * 通道响应ID
     */
    private String channelResponseId;
    /**
     * 通道状态报告Id
     */
    private String channelReportId;

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

    public String getExtendedCode() {
        return extendedCode;
    }

    public void setExtendedCode(String extendedCode) {
        this.extendedCode = extendedCode;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }

    public BigDecimal getCharge() {
        return charge;
    }

    public void setCharge(BigDecimal charge) {
        this.charge = charge;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getInterfaceServiceNo() {
        return interfaceServiceNo;
    }

    public void setInterfaceServiceNo(String interfaceServiceNo) {
        this.interfaceServiceNo = interfaceServiceNo;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getChannelResponseId() {
        return channelResponseId;
    }

    public void setChannelResponseId(String channelResponseId) {
        this.channelResponseId = channelResponseId;
    }

    public String getChannelReportId() {
        return channelReportId;
    }

    public void setChannelReportId(String channelReportId) {
        this.channelReportId = channelReportId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}
