package cn.emay.boot.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import cn.emay.task.TaskScheduler;
import cn.emay.task.TaskScheduler.TaskToStart;
import cn.emay.task.core.realize.concurrent.ConcurrentTask;
import cn.emay.task.core.realize.concurrentbind.ConcurrentBindTask;
import cn.emay.task.core.realize.cron.CronTask;
import cn.emay.task.core.realize.normal.Task;

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

	private TaskInfo[] tasks;

	/**
	 * 通过配置文件读取任务<br/>
	 * 也可以自己使用TaskScheduler.getInstance().loadxxxTaskxxx()加载
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Bean(name = "emayTaskScheduler")
	public TaskScheduler taskScheduler() {
		if (tasks == null || tasks.length == 0) {
			return TaskScheduler.getInstance();
		}
		List<TaskToStart> starts = new ArrayList<TaskScheduler.TaskToStart>();
		for (TaskInfo info : tasks) {
			String className = info.getClassName();
			if (className == null || "".equals(className)) {
				throw new IllegalArgumentException("className is null");
			}
			String name = info.getName();
			try {
				TaskToStart start = null;
				Class<?> taskClass = Class.forName(className);
				if (ConcurrentTask.class.isAssignableFrom(taskClass)) {
					start = TaskScheduler.getInstance().loadConcurrentTask(name, (Class<ConcurrentTask>) taskClass, info.getMinThread(), info.getMaxThread(), info.getResizeIntervalSecond());
				} else if (ConcurrentBindTask.class.isAssignableFrom(taskClass)) {
					start = TaskScheduler.getInstance().loadConcurrentBindTask(name, (Class<ConcurrentBindTask>) taskClass, info.getMaxThreadEvery(), info.getResizeIntervalSecond());
				} else if (CronTask.class.isAssignableFrom(taskClass)) {
					start = TaskScheduler.getInstance().loadCronTask(name, (Class<CronTask>) taskClass, info.getCron());
				} else if (Task.class.isAssignableFrom(taskClass)) {
					start = TaskScheduler.getInstance().loadTask(name, (Class<Task>) taskClass);
				} else {
					throw new IllegalArgumentException(className + "is not task");
				}
				if (info.isStart()) {
					starts.add(start);
				}
			} catch (Exception e) {
				throw new IllegalArgumentException(className + " load error");
			}
		}
		starts.stream().forEach(start -> start.andStart());
		return TaskScheduler.getInstance();
	}

	public TaskInfo[] getTasks() {
		return tasks;
	}

	public void setTasks(TaskInfo[] tasks) {
		this.tasks = tasks;
	}

	/**
	 * 任务参数
	 * 
	 * @author Frank
	 *
	 */
	public static class TaskInfo {
		/**
		 * 任务名
		 */
		private String name;
		/**
		 * 任务class名
		 */
		private String className;
		/**
		 * 每个参数的最大线程数
		 */
		private int maxThreadEvery;
		/**
		 * 线程调整时间间隔
		 */
		private int resizeIntervalSecond;
		/**
		 * 最大线程数
		 */
		private int maxThread;
		/**
		 * 最小线程数
		 */
		private int minThread;
		/**
		 * 表达式
		 */
		private String cron;
		/**
		 * 是否启动
		 */
		private boolean start = true;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public int getMaxThreadEvery() {
			return maxThreadEvery;
		}

		public void setMaxThreadEvery(int maxThreadEvery) {
			this.maxThreadEvery = maxThreadEvery;
		}

		public int getMaxThread() {
			return maxThread;
		}

		public void setMaxThread(int maxThread) {
			this.maxThread = maxThread;
		}

		public int getMinThread() {
			return minThread;
		}

		public void setMinThread(int minThread) {
			this.minThread = minThread;
		}

		public String getCron() {
			return cron;
		}

		public void setCron(String cron) {
			this.cron = cron;
		}

		public int getResizeIntervalSecond() {
			return resizeIntervalSecond;
		}

		public void setResizeIntervalSecond(int resizeIntervalSecond) {
			this.resizeIntervalSecond = resizeIntervalSecond;
		}

		public boolean isStart() {
			return start;
		}

		public void setStart(boolean start) {
			this.start = start;
		}

	}

}
