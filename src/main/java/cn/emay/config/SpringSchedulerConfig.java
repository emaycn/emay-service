package cn.emay.config;

import cn.emay.constant.task.ConcurrentComputer;
import cn.emay.constant.task.ConcurrentFieldComputer;
import cn.emay.constant.task.SuperScheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *
 */
@Configuration
@EnableScheduling
public class SpringSchedulerConfig implements SchedulingConfigurer {

    /**
     * log
     */
    private static final Logger LOG = LoggerFactory.getLogger(SpringSchedulerConfig.class);
    /**
     * 线程池核心线程数
     */
    @Value("${scheduler.poolSize}")
    private int poolSize;
    /**
     * 线程池线程名称前缀
     */
    @Value("${scheduler.threadNamePrefix}")
    private String threadNamePrefix;
    /**
     * 线程池停止时等待业务执行完毕时间
     */
    @Value("${scheduler.awaitTerminationSeconds}")
    private int awaitTerminationSeconds;
    /**
     * 默认的属性名
     */
    protected static final String DEFAULT_FIELD = "_32_DEFAULT_1024_";
    /**
     * 扫描到的SuperScheduled临时存放容器
     */
    private static List<SuperScheduledExec> TEMP_WAIT_TASKS = new ArrayList<>();
    /**
     * 动态任务容器
     */
    private static final Map<String, Map<String, List<ScheduledItem>>> DYNAMIC_TASKS = new HashMap<>();
    /**
     * 缓存spring的scheduler注册器
     */
    private static ScheduledTaskRegistrar TASK_REGISTRAR;
    /**
     * 缓存spring context
     */
    private static ApplicationContext APPLICATION_CONTEXT;

    /**
     * 自定义线程池<br/>
     * 如果线程池不满足，在此处自定义
     */
    @Bean
    public ThreadPoolTaskScheduler newThreadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(Math.max(1, poolSize));
        scheduler.setThreadNamePrefix(threadNamePrefix);
        scheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        scheduler.setAwaitTerminationSeconds(Math.max(0, awaitTerminationSeconds));
        scheduler.initialize();
        return scheduler;
    }

    /**
     * 往spring的scheduler注册器添加自定义的线程池和SuperScheduled任务
     *
     * @param taskRegistrar spring的scheduler注册器
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        SpringSchedulerConfig.TASK_REGISTRAR = taskRegistrar;
        ThreadPoolTaskScheduler scheduler = newThreadPoolTaskScheduler();
        taskRegistrar.setTaskScheduler(scheduler);
        TEMP_WAIT_TASKS.forEach(exec -> {
            taskRegistrar.addTriggerTask(exec.getTask(), exec.getTrigger());
            if (LOG.isDebugEnabled()) {
                LOG.debug("add task " + exec.getId() + "," + exec.getTaskName());
            }
        });
        TEMP_WAIT_TASKS = null;
    }

    /**
     * 缓存spring context
     */
    @Component
    public static class ApplicationContextConfig implements ApplicationContextAware {
        public ApplicationContextConfig() {
        }

        public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
            APPLICATION_CONTEXT = applicationContext;
        }
    }

    /**
     * 在spring初始化bean完毕后，加载自定义的SuperScheduled任务处理逻辑
     */
    @Component
    public static class TaskSupportBeanPostProcessor implements BeanPostProcessor {

        @Override
        public Object postProcessAfterInitialization(Object bean, @NonNull String beanName) throws BeansException {
            for (Method method : bean.getClass().getDeclaredMethods()) {
                SuperScheduled scheduled = method.getAnnotation(SuperScheduled.class);
                if (scheduled != null) {
                    String name = "@SuperScheduled[" + bean.getClass().getName() + ":" + method.getName() + "]";
                    if (!method.getReturnType().equals(long.class)) {
                        throw new IllegalArgumentException("method return long in " + name);
                    }
                    long initialDelay = Math.max(scheduled.initialDelay(), 0L);
                    long dynamicConcurrentDelay = scheduled.dynamicConcurrentDelay();
                    if (dynamicConcurrentDelay > 0) {
                        String computerName = scheduled.dynamicConcurrentBean();
                        if ("".equals(computerName)) {
                            throw new IllegalArgumentException("dynamicConcurrentDelay > 0 , but dynamicConcurrentBean is null : " + name);
                        }
                        Object computer = APPLICATION_CONTEXT.getBean(computerName);
                        if (ConcurrentComputer.class.isAssignableFrom(computer.getClass())) {
                            if (method.getParameterCount() != 0) {
                                throw new IllegalArgumentException("method must has no param in " + name);
                            }
                            DynamicComputeTask dct = new DynamicComputeTask(name, (ConcurrentComputer) computer, dynamicConcurrentDelay, bean, method, initialDelay, scheduled.dynamicConcurrentMax());
                            Method me = SpringSchedulerConfig.getMethod(DynamicComputeTask.class, DynamicComputeTask.METHOD);
                            SuperScheduledExec exec = new SuperScheduledExec(dct, me, DEFAULT_FIELD, 1000L, name);
                            TEMP_WAIT_TASKS.add(exec);
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("load DynamicComputeTask " + name + " with " + dynamicConcurrentDelay + " dynamicConcurrentDelay.");
                            }
                        } else if (ConcurrentFieldComputer.class.isAssignableFrom(computer.getClass())) {
                            if (method.getParameterCount() != 1) {
                                throw new IllegalArgumentException("method must has one string param in " + name);
                            }
                            if (!method.getParameterTypes()[0].getName().equals(String.class.getName())) {
                                throw new IllegalArgumentException("method must has one string param in " + name);
                            }
                            DynamicFieldComputeTask dct = new DynamicFieldComputeTask(name, (ConcurrentFieldComputer) computer, dynamicConcurrentDelay, bean, method, initialDelay, scheduled.dynamicConcurrentMax());
                            Method me = SpringSchedulerConfig.getMethod(DynamicFieldComputeTask.class, DynamicFieldComputeTask.METHOD);
                            SuperScheduledExec exec = new SuperScheduledExec(dct, me, DEFAULT_FIELD, 1000L, name);
                            TEMP_WAIT_TASKS.add(exec);
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("load DynamicFieldComputeTask " + name + " with " + dynamicConcurrentDelay + " dynamicConcurrentDelay.");
                            }
                        } else {
                            throw new IllegalArgumentException("dynamicConcurrentDelay > 0 , but dynamicConcurrentBean not support : " + name);
                        }
                    } else {
                        int concurrent = Math.max(scheduled.concurrent(), 1);
                        for (int i = 0; i < concurrent; i++) {
                            SuperScheduledExec exec = new SuperScheduledExec(bean, method, DEFAULT_FIELD, initialDelay, name);
                            TEMP_WAIT_TASKS.add(exec);
                        }
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("load task " + name + " with " + concurrent + " concurrent.");
                        }
                    }
                }
            }
            return bean;
        }
    }

    /**
     * 根据任务名从动态任务管理器中拿任务集合
     *
     * @param name 任务名
     * @return 任务集合
     */
    protected static Map<String, List<ScheduledItem>> getTasksByName(String name) {
        return DYNAMIC_TASKS.computeIfAbsent(name, k -> new HashMap<>());
    }

    /**
     * 根据任务名和属性名从动态任务管理器中拿任务集合
     *
     * @param name  任务名
     * @param field 属性名
     * @return 任务集合
     */
    protected static List<ScheduledItem> getTasksByNameAndField(String name, String field) {
        Map<String, List<ScheduledItem>> fieldMap = getTasksByName(name);
        return fieldMap.computeIfAbsent(field, k -> new ArrayList<>());
    }

    /**
     * 移除一个任务
     *
     * @param name  任务名
     * @param field 属性名
     */
    protected static void removeTask(String name, String field) {
        List<ScheduledItem> tasks = getTasksByNameAndField(name, field);
        if (tasks.size() == 0) {
            return;
        }
        ScheduledItem item = tasks.remove(0);
        ScheduledFuture<?> future = getFieldValue(ScheduledTask.class, "future", item.getScheduledTask());
        future.cancel(false);
        Set<ScheduledTask> scheduledTasks = getFieldValue(ScheduledTaskRegistrar.class, "scheduledTasks", TASK_REGISTRAR);
        scheduledTasks.remove(item.getScheduledTask());
        List<TriggerTask> triggerTasks = getFieldValue(ScheduledTaskRegistrar.class, "triggerTasks", TASK_REGISTRAR);
        triggerTasks.remove(item.getTriggerTask());
        if (LOG.isDebugEnabled()) {
            LOG.debug("dynamic remove task " + name + "-" + field);
        }
    }

    /**
     * 新增一个任务
     *
     * @param name               任务名
     * @param field              属性名
     * @param bean               任务执行对象
     * @param method             任务执行方法
     * @param initialDelayMillis 初始化延迟加载时间
     * @param concurrentMax      最大并发数
     */
    protected static void addTask(String name, String field, Object bean, Method method, long initialDelayMillis, int concurrentMax) {
        List<ScheduledItem> tasks = getTasksByNameAndField(name, field);
        if (concurrentMax > 0 && tasks.size() >= concurrentMax) {
            return;
        }
        SuperScheduledExec se = new SuperScheduledExec(bean, method, field, initialDelayMillis, name + "-" + field);
        TriggerTask tt = new TriggerTask(se.getTask(), se.getTrigger());
        ScheduledTask st = TASK_REGISTRAR.scheduleTriggerTask(tt);
        Method runMethod = getMethod(TASK_REGISTRAR.getClass(), "addScheduledTask", ScheduledTask.class);
        invokeMethod(TASK_REGISTRAR, runMethod, st);
        ScheduledItem item = new ScheduledItem(se, tt, st);
        tasks.add(item);
        if (LOG.isDebugEnabled()) {
            LOG.debug("dynamic add task " + name + "-" + field);
        }
    }

    /**
     * 反射获取方法，基础工具方法
     *
     * @param clazz          类
     * @param methodName     方法名
     * @param parameterTypes 方法参数类型集合
     * @return 方法
     */
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 反射执行方法，基础工具方法
     *
     * @param bean   对象
     * @param method 方法
     * @param args   参数
     * @param <T>    返回值类型
     * @return 返回值
     */
    @SuppressWarnings("unchecked")
    public static <T> T invokeMethod(Object bean, Method method, Object... args) {
        try {
            return (T) method.invoke(bean, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 反射获取字段值，基础工具方法
     *
     * @param clazz     类
     * @param fieldName 字段名
     * @param obj       对象
     * @param <T>       字段类型
     * @return 字段值
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Class<?> clazz, String fieldName, Object obj) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * map转字符串，基础工具方法
     *
     * @param map map
     * @return 字符串
     */
    public static String toString(Map<String, Integer> map) {
        StringBuilder builder = new StringBuilder();
        map.forEach((k, v) -> builder.append(k).append("=").append(v).append(";"));
        return builder.toString();
    }

    /**
     * 任务元数据
     */
    public static class ScheduledItem {

        /**
         * 执行单元
         */
        private SuperScheduledExec superScheduledExec;
        /**
         * 调度器
         */
        private TriggerTask triggerTask;
        /**
         * 任务
         */
        private ScheduledTask scheduledTask;

        /**
         * @param superScheduledExec 执行单元
         * @param triggerTask        调度器
         * @param scheduledTask      任务
         */
        public ScheduledItem(SuperScheduledExec superScheduledExec, TriggerTask triggerTask, ScheduledTask scheduledTask) {
            this.superScheduledExec = superScheduledExec;
            this.triggerTask = triggerTask;
            this.scheduledTask = scheduledTask;
        }

        public SuperScheduledExec getSuperScheduledExec() {
            return superScheduledExec;
        }

        public void setSuperScheduledExec(SuperScheduledExec superScheduledExec) {
            this.superScheduledExec = superScheduledExec;
        }

        public TriggerTask getTriggerTask() {
            return triggerTask;
        }

        public void setTriggerTask(TriggerTask triggerTask) {
            this.triggerTask = triggerTask;
        }

        public ScheduledTask getScheduledTask() {
            return scheduledTask;
        }

        public void setScheduledTask(ScheduledTask scheduledTask) {
            this.scheduledTask = scheduledTask;
        }

    }

    /**
     * 任务执行器
     */
    public static class SuperScheduledExec {

        /**
         * 日志
         */
        private final Logger log = LoggerFactory.getLogger(getClass());
        /**
         * 初始化延时时间，固定值，用以后续动态扩展新执行单元使用
         */
        private final long initialDelayMillis;
        /**
         * 执行对象
         */
        private final Method method;
        /**
         * 执行方法
         */
        private final Object bean;
        /**
         * 执行逻辑
         */
        private final Runnable runnable;
        /**
         * 执行触发器
         */
        private final Trigger trigger;
        /**
         * ID
         */
        private final String id;
        /**
         * 任务名称
         */
        private final String taskName;
        /**
         * 属性
         */
        private final String field;

        /**
         * 初始化延时，仅在创建执行单元后第一次执行生效
         */
        private Long initialDelay;
        /**
         * 执行间隔，每次执行后都会刷新此值
         */
        private long delayMillis = 0L;

        /**
         * @param bean               执行对象
         * @param method             执行方法
         * @param initialDelayMillis 初始化延时时间
         * @param taskName           任务名称
         */
        public SuperScheduledExec(Object bean, Method method, String field, long initialDelayMillis, String taskName) {
            this.id = UUID.randomUUID().toString();
            this.bean = bean;
            this.method = method;
            this.field = field;
            this.taskName = taskName;
            this.initialDelayMillis = initialDelayMillis;
            this.initialDelay = initialDelayMillis;
            this.runnable = () -> {
                try {
                    if (SpringSchedulerConfig.DEFAULT_FIELD.equals(this.field)) {
                        this.delayMillis = SpringSchedulerConfig.invokeMethod(bean, method);
                    } else {
                        this.delayMillis = SpringSchedulerConfig.invokeMethod(bean, method, this.field);
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("exec task " + taskName + " once ok");
                    }
                } catch (Exception e) {
                    this.delayMillis = 1000L;
                    log.error("exec task: " + taskName + " error .", e);
                }
            };
            this.trigger = triggerContext -> {
                if (initialDelay == null) {
                    return new Date(this.delayMillis + System.currentTimeMillis());
                } else {
                    Date date = new Date(this.delayMillis + initialDelay + System.currentTimeMillis());
                    initialDelay = null;
                    return date;
                }
            };
        }

        public Object getBean() {
            return bean;
        }

        public Method getMethod() {
            return method;
        }

        public Runnable getTask() {
            return runnable;
        }

        public Trigger getTrigger() {
            return trigger;
        }

        public long getInitialDelay() {
            return initialDelayMillis;
        }

        public String getId() {
            return id;
        }

        public String getTaskName() {
            return taskName;
        }

        public String getField() {
            return field;
        }

    }

    /**
     * 动态计算并发线程数任务
     */
    public static class DynamicComputeTask {

        /**
         * 日志
         */
        private final Logger log = LoggerFactory.getLogger(getClass());
        /**
         * 任务执行方法名
         */
        public final static String METHOD = "compute";
        /**
         * 计算逻辑
         */
        private final ConcurrentComputer computer;
        /**
         * 计算时间间隔
         */
        private final long dynamicConcurrentDelay;
        /**
         * 任务名
         */
        private final String name;
        /**
         * 最大并发数
         */
        private final int concurrentMax;
        /**
         * 初始化延时时间，固定值，用以后续动态扩展新执行单元使用
         */
        private final long initialDelayMillis;
        /**
         * 执行对象
         */
        private final Method method;
        /**
         * 执行方法
         */
        private final Object bean;

        /**
         * @param name                   任务名
         * @param computer               计算逻辑
         * @param dynamicConcurrentDelay 计算时间间隔
         * @param bean                   执行对象
         * @param method                 执行方法
         * @param initialDelayMillis     初始化延时时间
         * @param concurrentMax          最大并发数
         */
        public DynamicComputeTask(String name, ConcurrentComputer computer, long dynamicConcurrentDelay, Object bean, Method method, long initialDelayMillis, int concurrentMax) {
            this.computer = computer;
            this.dynamicConcurrentDelay = dynamicConcurrentDelay;
            this.name = name;
            this.bean = bean;
            this.method = method;
            this.initialDelayMillis = initialDelayMillis;
            this.concurrentMax = concurrentMax;
        }

        /**
         * 计算
         *
         * @return 执行间隔时间
         */
        public long compute() {
            int concurrent = SpringSchedulerConfig.getTasksByNameAndField(name, SpringSchedulerConfig.DEFAULT_FIELD).size();
            int need = computer.compute(concurrent);
            if (need > concurrent) {
                int add = need - concurrent;
                for (int i = 0; i < add; i++) {
                    SpringSchedulerConfig.addTask(name, SpringSchedulerConfig.DEFAULT_FIELD, bean, method, initialDelayMillis, concurrentMax);
                }
            } else if (need < concurrent) {
                int sub = concurrent - need;
                for (int i = 0; i < sub; i++) {
                    SpringSchedulerConfig.removeTask(name, SpringSchedulerConfig.DEFAULT_FIELD);
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("compute once ok : concurrent(" + concurrent + ") -> need(" + need + ")");
            }
            return dynamicConcurrentDelay;
        }

    }

    /**
     * 动态计算属性并发线程数任务
     */
    public static class DynamicFieldComputeTask {

        /**
         * 日志
         */
        private final Logger log = LoggerFactory.getLogger(getClass());
        /**
         * 任务执行方法名
         */
        public final static String METHOD = "compute";
        /**
         * 计算逻辑
         */
        private final ConcurrentFieldComputer computer;
        /**
         * 计算时间间隔
         */
        private final long dynamicConcurrentDelay;
        /**
         * 任务名
         */
        private final String name;
        /**
         * 最大并发数
         */
        private final int concurrentMax;
        /**
         * 初始化延时时间，固定值，用以后续动态扩展新执行单元使用
         */
        private final long initialDelayMillis;
        /**
         * 执行对象
         */
        private final Method method;
        /**
         * 执行方法
         */
        private final Object bean;

        /**
         * @param name                   任务名
         * @param computer               计算逻辑
         * @param dynamicConcurrentDelay 计算时间间隔
         * @param bean                   执行对象
         * @param method                 执行方法
         * @param initialDelayMillis     初始化延时时间
         * @param concurrentMax          最大并发数
         */
        public DynamicFieldComputeTask(String name, ConcurrentFieldComputer computer, long dynamicConcurrentDelay, Object bean, Method method, long initialDelayMillis, int concurrentMax) {
            this.computer = computer;
            this.dynamicConcurrentDelay = dynamicConcurrentDelay;
            this.name = name;
            this.bean = bean;
            this.method = method;
            this.initialDelayMillis = initialDelayMillis;
            this.concurrentMax = concurrentMax;
        }

        /**
         * 计算
         *
         * @return 执行间隔时间
         */
        public long compute() {
            Map<String, List<ScheduledItem>> map = SpringSchedulerConfig.getTasksByName(name);
            Map<String, Integer> concurrent = new HashMap<>();
            if (map != null) {
                map.forEach((field, list) -> concurrent.put(field, list.size()));
            }
            Map<String, Integer> need = computer.compute(concurrent);

            Set<String> hasSet = new HashSet<>();
            Set<String> removeSet = new HashSet<>();
            Set<String> newSet = new HashSet<>();
            concurrent.keySet().forEach(field -> {
                if (need.containsKey(field)) {
                    hasSet.add(field);
                }
            });
            concurrent.keySet().forEach(field -> {
                if (!hasSet.contains(field)) {
                    removeSet.add(field);
                }
            });
            need.keySet().forEach(field -> {
                if (!hasSet.contains(field)) {
                    newSet.add(field);
                }
            });

            removeSet.forEach(field -> {
                int size = concurrent.get(field);
                for (int i = 0; i < size; i++) {
                    SpringSchedulerConfig.removeTask(name, field);
                }
            });
            hasSet.forEach(field -> {
                int theOld = concurrent.get(field);
                int theNew = need.get(field);
                if (theOld > theNew) {
                    int sub = theOld - theNew;
                    for (int i = 0; i < sub; i++) {
                        SpringSchedulerConfig.removeTask(name, field);
                    }
                } else if (theOld < theNew) {
                    int add = theNew - theOld;
                    for (int i = 0; i < add; i++) {
                        SpringSchedulerConfig.addTask(name, field, bean, method, initialDelayMillis, concurrentMax);
                    }
                }
            });
            newSet.forEach(field -> {
                int size = need.get(field);
                for (int i = 0; i < size; i++) {
                    SpringSchedulerConfig.addTask(name, field, bean, method, initialDelayMillis, concurrentMax);
                }
            });
            if (log.isDebugEnabled()) {
                log.debug("compute once ok : concurrent(" + SpringSchedulerConfig.toString(concurrent) + ") -> need(" + SpringSchedulerConfig.toString(need) + ")");
            }
            return dynamicConcurrentDelay;
        }

    }
}
