package cn.emay.constant.task;

import java.util.Map;

/**
 * 属性任务调整线程计算器 ，计算每个属性所需并发数
 */
public interface ConcurrentFieldComputer {

    /**
     * 计算每个属性所需并发数
     *
     * @return 每个属性所需并发数
     */
    Map<String, Integer> compute(Map<String, Integer> concurrent);
}
