package cn.emay.boot.dto.inter;

import java.util.Date;

import cn.emay.utils.id.OnlyIdGenerator;

public class SmsMessage {

	private Long id;

	private String appId;

	private String message;

	private String smsId;

	private String mobile;

	private Date createTime;

	public SmsMessage() {

	}

	public SmsMessage(String appId, String message, String mobile) {
		this.appId = appId;
		this.message = message;
		this.smsId = OnlyIdGenerator.genOnlyId("123");
		this.mobile = mobile;
		this.createTime = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSmsId() {
		return smsId;
	}

	public void setSmsId(String smsId) {
		this.smsId = smsId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
