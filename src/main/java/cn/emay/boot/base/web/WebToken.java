package cn.emay.boot.base.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.emay.boot.business.system.dto.SimpleUserDTO;
import cn.emay.boot.business.system.pojo.Resource;
import cn.emay.boot.business.system.pojo.User;
import cn.emay.boot.utils.ResourceTreeBuilder;
import cn.emay.utils.tree.EmaySimpleTreeNode;

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
	 * 用户资源树，用来构建左侧导航
	 */
	private EmaySimpleTreeNode<Long, Resource> userResourceTree;
	/**
	 * 用户资源字典，用来进行前端权限校验
	 */
	private Map<String, Boolean> userResourceMap;

	public WebToken() {

	}

	public WebToken(String sessionId, User user, List<Resource> resources) {
		this.sessionId = sessionId;
		this.user = new SimpleUserDTO(user);
		this.userResourceTree = ResourceTreeBuilder.buildWithoutOper(resources);
		this.userResourceMap = new HashMap<>();
		for (Resource resource : resources) {
			this.userResourceMap.put(resource.getResourceCode(), true);
		}

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

	public EmaySimpleTreeNode<Long, Resource> getUserResourceTree() {
		return userResourceTree;
	}

	public void setUserResourceTree(EmaySimpleTreeNode<Long, Resource> userResourceTree) {
		this.userResourceTree = userResourceTree;
	}

	public Map<String, Boolean> getUserResourceMap() {
		return userResourceMap;
	}

	public void setUserResourceMap(Map<String, Boolean> userResourceMap) {
		this.userResourceMap = userResourceMap;
	}

}
