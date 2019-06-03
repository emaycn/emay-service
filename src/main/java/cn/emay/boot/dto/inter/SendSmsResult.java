package cn.emay.boot.dto.inter;

public class SendSmsResult {
	
	private int status;
	private String message;
	private String smsId;

	public SendSmsResult() {

	}

	public SendSmsResult(int status, String message, String smsId) {
		this.status = status;
		this.message = message;
		this.smsId = smsId;
	}

	public static SendSmsResult errorResult(String message) {
		return new SendSmsResult(0, message, null);
	}
	
	public static SendSmsResult successResult(String smsId) {
		return new SendSmsResult(1, "SUCCESS", smsId);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

}
