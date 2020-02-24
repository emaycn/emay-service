package cn.emay.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import cn.emay.task.spring.TaskSchedulerSpring;

/**
 * Task组件配置
 * 
 * @author Frank
 *
 */
@Configuration
@ConfigurationProperties(prefix = "scheduler")
@Order(99)
public class SchedulerConfig {

	/**
	 * 所有任务
	 */
	private String[] tasks;

	/**
	 * 通过配置文件读取任务<br/>
	 * 需要自己启动
	 * 
	 * @return
	 */
	@Bean(name = "TaskSchedulerSpring",destroyMethod = "stop")
	public TaskSchedulerSpring taskScheduler() {
		return new TaskSchedulerSpring(tasks);
	}

	public String[] getTasks() {
		return tasks;
	}

	public void setTasks(String[] tasks) {
		this.tasks = tasks;
	}

}
