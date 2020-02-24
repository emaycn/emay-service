package cn.emay.business.sms.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信信息
 *
 * @author Frank
 */
public class SmsMessageDto implements Serializable {
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
     * id 等同于 smsid
     */
    private String smsId;
    /**
     * 手机
     */
    private String mobile;
    /**
	 * 扩展码
	 */
	private String extendedCode;
	/**
	 * 客户自定义 SMS ID
	 */
	private String customId;
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
     * 批次号
     */
    private String batchNo;
    /**
     * 状态：1-待发送，2-发送中，3-已发送，4-发送成功，5-发送失败，6-发送超时
     */
    private Integer state;
    /**
     * 定时时间
     */
    private Date timerTime;
    /**
	 * 计费条数
	 */
	private Integer cost;
	/**
     * 运营商状态报告代码
     */
    private String reportCode;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getTimerTime() {
        return timerTime;
    }

    public void setTimerTime(Date timerTime) {
        this.timerTime = timerTime;
    }

	public String getExtendedCode() {
		return extendedCode;
	}

	public void setExtendedCode(String extendedCode) {
		this.extendedCode = extendedCode;
	}

	public String getCustomId() {
		return customId;
	}

	public void setCustomId(String customId) {
		this.customId = customId;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	public String getSmsId() {
		return smsId;
	}

	public void setSmsId(String smsId) {
		this.smsId = smsId;
	}

}
