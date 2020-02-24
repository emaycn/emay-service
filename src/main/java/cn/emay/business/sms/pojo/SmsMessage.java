package cn.emay.business.sms.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cn.emay.elasticsearch.ann.EsField;
import cn.emay.elasticsearch.ann.EsFieldType;
import cn.emay.elasticsearch.ann.EsIndex;

/**
 * 短信信息
 * 
 * @author Frank
 *
 */
@EsIndex(name = "sms_message_")
public class SmsMessage implements Serializable {
	// 发送中
	public static final int STATE_HAS_SEND = 3;
	// 成功
	public static final int STATE_DELIVRD = 4;
	// 失败
	public static final int STATE_UNDELIIV = 5;
	// 超时
	public static final int STATE_UNKNOWN = 6;

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
	@EsField(type = EsFieldType.LONG)
	private Long id;
	/**
	 * 手机
	 */
	@EsField(type = EsFieldType.KEYWORD)
	private String mobile;
	/**
	 * 扩展码
	 */
	@EsField(type = EsFieldType.KEYWORD)
	private String extendedCode;
	/**
	 * APP KEY
	 */
	@EsField(type = EsFieldType.KEYWORD)
	private String appKey;
	/**
	 * APP 扩展
	 */
	@EsField(type = EsFieldType.KEYWORD)
	private String appCode;
	/**
	 * 发送类型：1-接口、2-页面
	 */
	@EsField(type = EsFieldType.INTEGER)
	private Integer sendType;
	/**
	 * 提交时间
	 */
	@EsField(type = EsFieldType.DATE)
	private Date submitTime;
	/**
	 * 内容
	 */
	@EsField(type = EsFieldType.TEXT)
	private String content;
	/**
	 * SMS ID
	 */
	@EsField(type = EsFieldType.KEYWORD)
	private String smsId;
	/**
	 * 客户自定义 SMS ID
	 */
	@EsField(type = EsFieldType.KEYWORD)
	private String customId;
	/**
	 * 接口编码
	 */
	@EsField(type = EsFieldType.KEYWORD)
	private String interfaceServiceNo;
	/**
	 * 批次号
	 */
	@EsField(type = EsFieldType.KEYWORD)
	private String batchNo;
	/**
	 * 客户IP
	 */
	@EsField(type = EsFieldType.KEYWORD)
	private String remoteIp;
	/**
	 * 通道ID
	 */
	@EsField(type = EsFieldType.INTEGER)
	private Long channelId;
	/**
	 * 运营商CODE
	 */
	@EsField(type = EsFieldType.KEYWORD)
	private String operatorCode;
	/**
	 * 省份Code
	 */
	@EsField(type = EsFieldType.KEYWORD)
	private String provinceCode;
	/**
	 * 城市
	 */
	@EsField(type = EsFieldType.KEYWORD)
	private String city;
	/**
	 * 计费条数
	 */
	@EsField(type = EsFieldType.INTEGER)
	private Integer cost;
	/**
	 * 状态：1-待发送，2-发送中，3-已发送，4-发送成功，5-发送失败
	 */
	@EsField(type = EsFieldType.INTEGER)
	private Integer state;
	/**
	 * 发送时间
	 */
	@EsField(type = EsFieldType.DATE)
	private Date sendTime;
	/**
	 * 状态报告返回时间
	 */
	@EsField(type = EsFieldType.DATE)
	private Date reportTime;
	/**
	 * 响应返回时间
	 */
	@EsField(type = EsFieldType.DATE)
	private Date responseTime;
	/**
	 * 运营商响应代码
	 */
	@EsField(type = EsFieldType.KEYWORD)
	private String responseCode;
	/**
	 * 运营商状态报告代码
	 */
	@EsField(type = EsFieldType.KEYWORD)
	private String reportCode;
	/**
	 * 最终的状态代码【自身定义】
	 */
	@EsField(type = EsFieldType.KEYWORD)
	private String resultCode;
	/**
	 * app计费
	 */
	@EsField(type = EsFieldType.DOUBLE)
	private BigDecimal charge;
	/**
	 * 通道消费
	 */
	@EsField(type = EsFieldType.DOUBLE)
	private BigDecimal channelCharge;
	/**
	 * 是否过审核
	 */
	@EsField(type = EsFieldType.BOOLEAN)
	private Boolean isAuditing;
	/**
	 * 审核人ID
	 */
	@EsField(type = EsFieldType.LONG)
	private Long auditingUserId;
	/**
	 * 是否校正
	 */
	@EsField(type = EsFieldType.BOOLEAN)
	private Boolean isRegulate;
	/**
	 * 定时时间
	 */
	@EsField(type = EsFieldType.DATE)
	private Date timerTime;
	/**
	 * 应用单价
	 */
	@EsField(type = EsFieldType.DOUBLE)
	private BigDecimal price;
	/**
	 * 通道单价
	 */
	@EsField(type = EsFieldType.DOUBLE)
	private BigDecimal channelPrice;
	/**
	 * 优先级(1-高，2-低)
	 */
	@EsField(type = EsFieldType.INTEGER)
	private Integer priority;
	/**
	 * 客户ID
	 */
	@EsField(type = EsFieldType.LONG)
	private Long clientId;
	/**
	 * 通道响应ID
	 */
	@EsField(type = EsFieldType.KEYWORD)
	private String channelResponseId;
	/**
	 * 通道状态报告Id
	 */
	@EsField(type = EsFieldType.KEYWORD)
	private String channelReportId;
	/**
	 * 进入通道队列的时间
	 */
	@EsField(type = EsFieldType.DATE)
	private Date pushChannelQueueTime;
	/**
	 * 入库时间，后续状态报告回来以后知道更新哪个库
	 */
	@EsField(type = EsFieldType.DATE)
	private Date saveTime;
	/**
	 * 是否发送到通道
	 */
	@EsField(type = EsFieldType.BOOLEAN)
	private Boolean isRealSend;

	/**
	 * 是否被补发<br/>
	 * 放弃统计、退款、导出、再次补发
	 */
	@EsField(type = EsFieldType.BOOLEAN)
	private Boolean isReissue;

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

	public Boolean getIsAuditing() {
		return isAuditing;
	}

	public void setIsAuditing(Boolean isAuditing) {
		this.isAuditing = isAuditing;
	}

	public Long getAuditingUserId() {
		return auditingUserId;
	}

	public void setAuditingUserId(Long auditingUserId) {
		this.auditingUserId = auditingUserId;
	}

	public Boolean getIsRegulate() {
		return isRegulate;
	}

	public void setIsRegulate(Boolean isRegulate) {
		this.isRegulate = isRegulate;
	}

	public Date getTimerTime() {
		return timerTime;
	}

	public void setTimerTime(Date timerTime) {
		this.timerTime = timerTime;
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

	public BigDecimal getChannelPrice() {
		return channelPrice;
	}

	public void setChannelPrice(BigDecimal channelPrice) {
		this.channelPrice = channelPrice;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
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

	public Date getPushChannelQueueTime() {
		return pushChannelQueueTime;
	}

	public void setPushChannelQueueTime(Date pushChannelQueueTime) {
		this.pushChannelQueueTime = pushChannelQueueTime;
	}

	public Date getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(Date saveTime) {
		this.saveTime = saveTime;
	}

	public BigDecimal getChannelCharge() {
		return channelCharge;
	}

	public void setChannelCharge(BigDecimal channelCharge) {
		this.channelCharge = channelCharge;
	}

	public Boolean getIsRealSend() {
		return isRealSend;
	}

	public void setIsRealSend(Boolean isRealSend) {
		this.isRealSend = isRealSend;
	}

	public Boolean getIsReissue() {
		return isReissue;
	}

	public void setIsReissue(Boolean isReissue) {
		this.isReissue = isReissue;
	}
}
