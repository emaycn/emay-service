package cn.emay.constant.task;

import java.lang.annotation.*;

/**
 * Spring @SuperScheduled 扩展<br/>
 * 1. 支持动态执行间隔时间<br/>
 * 2. 支持初始化延时<br/>
 * 3. 支持多线程并发<br/>
 * 4. 支持属性粒度的多线程并发<br/>
 * 5. 支持动态调整线程数<br/>
 *
 * @author frank
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SuperScheduled {

    /**
     * 初始化延时时间，单位毫秒<br/>
     * 任务初始化以后，延迟此时间以后执行。<br/>
     * 最小为0。<br/>
     * 通用属性
     */
    long initialDelay() default 0L;

    /**
     * 并发数<br/>
     * 任务并发数量，多线程并发执行。<br/>
     * 最小并发数为1。<br/>
     * 非通用属性，优先级低，与dynamicConcurrentDelay、dynamicConcurrentBean、dynamicConcurrentMax互斥
     */
    int concurrent() default 1;

    /**
     * 动态调整线程数间隔时间，单位毫秒<br/>
     * 每间隔此时间，调用${computeConcurrentBean}计算当前所需并发数，并调整当前并发线程数。<br/>
     * 如果<=0,则不进行动态调整。<br/>
     * 非通用属性，与concurrent互斥，优先级高于concurrent，必须和dynamicConcurrentBean、dynamicConcurrentMax配合使用
     */
    long dynamicConcurrentDelay() default -1;

    /**
     * 动态计算线程数的Bean<br/>
     * ConcurrentComputer ： 普通并发任务计算器<br/>
     * ConcurrentBindComputer ： 属性并发任务计算器<br/>
     * 非通用属性，与concurrent互斥，优先级高于concurrent，必须和dynamicConcurrentDelay、dynamicConcurrentMax配合使用
     */
    String dynamicConcurrentBean() default "";

    /**
     * 动态并发线程总数最大值,防止dynamicConcurrentBean返回过大造成崩溃<br/>
     * 如果<=0不限制<br/>
     * 非通用属性，与concurrent互斥，优先级高于concurrent，必须和dynamicConcurrentDelay、dynamicConcurrentBean配合使用
     */
    int dynamicConcurrentMax() default -1;

}
