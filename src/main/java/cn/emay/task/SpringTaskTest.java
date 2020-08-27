package cn.emay.task;


import cn.emay.constant.task.ConcurrentComputer;
import cn.emay.constant.task.ConcurrentFieldComputer;
import cn.emay.constant.task.SuperScheduled;
import cn.emay.http.apache.HttpClientUtils;
import cn.emay.json.JsonHelper;
import cn.emay.utils.date.DateUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 配置文件中<br/>
 * scheduler.poolSize 核心线程数<br/>
 * scheduler.threadNamePrefix 线程名前缀<br/>
 * scheduler.awaitTerminationSeconds 停止时等待当前线程业务执行完毕时间
 */
@Component
public class SpringTaskTest {

    /**
     * 模拟长事务，测试停止应用不会造成业务中断<br/>
     */
    private void testLongTime() {
        for (int i = 0; i < 10; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("page_id", "4845534524194232");
            map.put("_item_pwd", "191919");
            HttpClientUtils.post("https://www.showdoc.cc/server/index.php?s=/api/page/info", map);
        }
    }

    /**
     * Spring @Scheduled
     * 1. 间隔固定n秒执行
     */
    @Scheduled(fixedDelay = 1000)
    public void t3() {
        // spring 原生支持，不再示例
    }

    /**
     * Spring @Scheduled
     * 2. 固定频率n秒执行
     */
    @Scheduled(fixedRate = 5000)
    public void t2() {
        // spring 原生支持，不再示例
    }

    /**
     * Spring @Scheduled
     * 3. cron执行
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void t1() {
        // spring 原生支持，不再示例
    }


    /**
     * Spring 增强 @SuperScheduled
     * 4. 支持动态执行间隔时间
     */
    @SuperScheduled
    public long t4() {
        String now = DateUtils.toString(new Date(), "HH:mm:ss");
        System.out.println(now + " : " + Thread.currentThread().getName() + " : 开始执行");
        testLongTime();
        now = DateUtils.toString(new Date(), "HH:mm:ss");
        int next = new Random().nextInt(5);
        System.out.println(now + " : " + Thread.currentThread().getName() + " : 执行完成，" + next + "秒后再次执行");
        return next * 1000L;
    }

    /**
     * 5. 并发
     */
    @SuperScheduled(concurrent = 4, initialDelay = 1000L)
    public long t5() {
        String now = DateUtils.toString(new Date(), "HH:mm:ss");
        System.out.println(now + " : " + Thread.currentThread().getName() + " : 开始执行");
        testLongTime();
        now = DateUtils.toString(new Date(), "HH:mm:ss");
        int next = new Random().nextInt(4);
        System.out.println(now + " : " + Thread.currentThread().getName() + " : 执行完成，" + next + "秒后再次执行");
        return next * 1000L;
    }

    /**
     * 6. 动态调配并发数量
     */
    @SuperScheduled(dynamicConcurrentDelay = 10000L, dynamicConcurrentBean = "t6ComputeBean", dynamicConcurrentMax = 4)
    public long t6() {
        String now = DateUtils.toString(new Date(), "HH:mm:ss");
        System.out.println(now + " : " + Thread.currentThread().getName() + " : 开始执行");
        testLongTime();
        now = DateUtils.toString(new Date(), "HH:mm:ss");
        int next = 1;
        System.out.println(now + " : " + Thread.currentThread().getName() + " : 执行完成，" + next + "秒后再次执行");
        return next * 1000L;
    }

    /**
     * 6. 动态调配并发数量
     */
    @Bean("t6ComputeBean")
    public ConcurrentComputer t6ComputeBean() {
        return concurrent -> {
            String now = DateUtils.toString(new Date(), "HH:mm:ss");
            int need = new Random().nextInt(8);
            System.out.println(now + " : " + Thread.currentThread().getName() + " : " + "t6ComputeBean 开始调整线程,当前并发量(" + concurrent + "),期望并发量(" + need + ")");
            return need;
        };
    }

    /**
     * 7. 属性并发<br/>
     * 计算每个属性所需并发量，并在执行任务时传递属性给执行方法。
     *
     * @param field 属性
     */
    @SuperScheduled(dynamicConcurrentDelay = 10000L, dynamicConcurrentBean = "t7ComputeBean", dynamicConcurrentMax = 8)
    public long t7(String field) {
        String now = DateUtils.toString(new Date(), "HH:mm:ss");
        System.out.println(now + " : " + Thread.currentThread().getName() + " : 开始执行 by " + field);
        testLongTime();
        now = DateUtils.toString(new Date(), "HH:mm:ss");
        int next = 2;
        System.out.println(now + " : " + Thread.currentThread().getName() + " : 执行完成，" + next + "秒后再次执行 by " + field);
        return next * 1000L;
    }

    static AtomicInteger integer = new AtomicInteger();

    /**
     * 7. 动态调配属性并发数量
     */
    @Bean("t7ComputeBean")
    public ConcurrentFieldComputer t7ComputeBean() {
        return concurrent -> {
            String nowConcurrent = JsonHelper.toJsonString(concurrent);

            Map<String, Integer> need = new HashMap<>();

            int i = integer.addAndGet(1);
            switch (i) {
                case 1:
                    need.put("key1", 1);
                    break;
                case 2:
                    need.put("key2", 1);
                    break;
                case 3:
                    need.put("key3", 1);
                    break;
                case 4:
                    need.put("key4", 1);
                    need.put("key5", 1);
                    break;
                case 5:
                    need.put("key4", 2);
                    need.put("key2", 2);
                    break;
                case 6:
                    need.put("key6", 2);
                    need.put("key1", 2);
                    break;
                default:
                    need.put("key10", 20);
                    break;
            }

            String needConcurrent = JsonHelper.toJsonString(need);

            String now = DateUtils.toString(new Date(), "HH:mm:ss");
            System.out.println(now + " : " + Thread.currentThread().getName() + " : " + "t7ComputeBean 开始调整线程,当前并发量(" + nowConcurrent + "),期望并发量(" + needConcurrent + ")");
            return need;
        };
    }


}
