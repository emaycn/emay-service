package cn.emay.boot.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.emay.boot.common.FileStoreCommon;
import cn.emay.boot.common.RedisKeys;
import cn.emay.boot.dto.inter.SmsMessage;
import cn.emay.boot.utils.ApplicationContextUtils;
import cn.emay.boot.utils.FileStoreUtils;
import cn.emay.json.JsonHelper;
import cn.emay.redis.RedisClient;
import cn.emay.store.file.queue.FileQueue;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.realize.cron.CronTask;

/**
 * 从文件队列中拉取短信，存储到redis中
 * 
 * @author Frank
 *
 */
public class CacheFiletoRedisTask implements CronTask {

	/**
	 * 日志
	 */
	private Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public TaskResult exec() {
		log.info(Thread.currentThread().getName() + " save begin ");
		FileQueue queue = FileStoreUtils.getQueue(FileStoreCommon.CACHE_SMS_QUEUE);
		RedisClient redis = ApplicationContextUtils.getBean(RedisClient.class);
		String smsStr = null;
		while ((smsStr = queue.poll()) != null) {
			SmsMessage message = JsonHelper.fromJson(SmsMessage.class, smsStr);
			redis.lpush(RedisKeys.SMS_LIST_KEY + message.getAppId(), -1, message);
			log.info(Thread.currentThread().getName() + " save sms[" + smsStr + "] to redis from file queue");
		}
		log.info(Thread.currentThread().getName() + " save end");
		return TaskResult.doBusinessSuccessResult();
	}

}
