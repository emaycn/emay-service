package cn.emay.boot.base.web;

import java.io.Serializable;
import java.util.List;

import cn.emay.boot.business.system.pojo.Resource;

public class WebToken implements Serializable {

	private static final long serialVersionUID = 1L;

	public final static int WEB_AUTH_TICKET_TIMEOUT = 30 * 60;

	private String sessionId;

	private Long userId;

	private List<Resource> auth;

	public WebToken() {

	}

	public WebToken(String sessionId, Long userId, List<Resource> auth) {
		this.userId = userId;
		this.sessionId = sessionId;
		this.auth = auth;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public List<Resource> getAuth() {
		return auth;
	}

	public void setAuth(List<Resource> auth) {
		this.auth = auth;
	}

}
