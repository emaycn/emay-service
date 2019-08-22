package cn.emay.boot.base.web;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.emay.boot.business.system.dto.SimpleUserDTO;
import cn.emay.boot.business.system.pojo.Resource;
import cn.emay.boot.business.system.pojo.User;

/**
 * 用户登录Token
 * 
 * @author Frank
 *
 */
public class WebToken implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 用户登录标识
	 */
	private String sessionId;
	/**
	 * 用户信息
	 */
	private SimpleUserDTO user;
	/**
	 * 用户资源字典，用来进行前端权限校验
	 */
	private Map<String, Boolean> resources;

	/**
	 * 创建时间
	 */
	private Date createTime;

	public WebToken() {
		this.createTime = new Date();
	}

	public WebToken(String sessionId, User user, List<Resource> resources) {
		this.sessionId = sessionId;
		this.user = new SimpleUserDTO(user);
		Map<String, Boolean> resource = new HashMap<>();
		for (Resource res : resources) {
			resource.put(res.getResourceCode(), true);
		}
		this.resources = resource;
		this.createTime = new Date();
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public SimpleUserDTO getUser() {
		return user;
	}

	public void setUser(SimpleUserDTO user) {
		this.user = user;
	}

	public Map<String, Boolean> getResources() {
		return resources;
	}

	public void setResources(Map<String, Boolean> resources) {
		this.resources = resources;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
