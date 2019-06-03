package cn.emay.boot.utils;

import java.util.HashMap;
import java.util.Map;

import cn.emay.boot.config.PropertiesConfig;
import cn.emay.store.file.queue.FileQueue;

/**
 * 本地存储工具
 * 
 * @author Frank
 *
 */
public class FileStoreUtils {

	/**
	 * 队列集合
	 */
	private static final Map<String, FileQueue> QUEUES = new HashMap<>();

	/**
	 * 获取短信接收队列
	 * 
	 * @return
	 */
	public static synchronized FileQueue getQueue(String queueName) {
		PropertiesConfig config = ApplicationContextUtils.getBean(PropertiesConfig.class);
		FileQueue queue = QUEUES.get(queueName);
		if (queue == null) {
			queue = new FileQueue(config.getStoreQueueBaseDir() + queueName);
			QUEUES.put(queueName, queue);
		}
		return queue;
	}

}
