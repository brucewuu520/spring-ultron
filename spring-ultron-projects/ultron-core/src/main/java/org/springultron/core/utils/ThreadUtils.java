package org.springultron.core.utils;

import java.util.concurrent.TimeUnit;

/**
 * 线程工具类
 *
 * @author brucewuu
 * @date 2021/1/28 下午4:10
 */
public class ThreadUtils {

    private ThreadUtils() {
    }

    /**
     * 线程休眠
     *
     * @param millis 休眠时长（毫秒）
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 线程休眠
     *
     * @param timeUnit 休眠时长单位
     * @param timeout  休眠时长
     */
    public static void sleep(TimeUnit timeUnit, long timeout) {
        try {
            timeUnit.sleep(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
