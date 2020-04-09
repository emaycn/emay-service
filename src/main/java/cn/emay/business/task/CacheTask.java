package cn.emay.business.task;

import cn.emay.redis.RedisClient;
import cn.emay.task.annotation.TaskInfo;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.realize.normal.Task;
import cn.emay.utils.ApplicationContextUtils;

@TaskInfo(name = "缓存任务")
public class CacheTask implements Task {

	@Override
	public TaskResult exec() {
		
		RedisClient redis = ApplicationContextUtils.getBean(RedisClient.class);
		System.out.println(redis.set("TEST-1","test",100));
		
		return TaskResult.doBusinessSuccessResult();
	}

	@Override
	public long runIntervalMillisecond() {
		return 1000L;
	}

}
