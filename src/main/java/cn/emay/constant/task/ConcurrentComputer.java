package cn.emay.constant.task;

/**
 * 任务调整线程计算器 ，计算任务所需并发数
 */
public interface ConcurrentComputer {

    /**
     * 计算任务所需并发数
     *
     * @param concurrent 当前并发量
     * @return 任务所需并发数
     */
    int compute(int concurrent);

}
