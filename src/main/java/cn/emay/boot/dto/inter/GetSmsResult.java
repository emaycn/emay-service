package cn.emay.boot.dto.inter;

import java.util.List;

public class GetSmsResult {

	private int status;
	private List<SmsMessage> messages;
	private String message;

	public GetSmsResult() {

	}

	public GetSmsResult(int status, String message, List<SmsMessage> messages) {
		this.status = status;
		this.messages = messages;
		this.message = message;
	}

	public static GetSmsResult errorResult(String message) {
		return new GetSmsResult(0, message, null);
	}

	public static GetSmsResult successResult(List<SmsMessage> messages) {
		return new GetSmsResult(1, "SUCCESS", messages);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<SmsMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<SmsMessage> messages) {
		this.messages = messages;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
