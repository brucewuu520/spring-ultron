package org.springultron.core.utils;

import java.sql.Timestamp;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 时钟工具
 * 高并发场景下System.currentTimeMillis()的性能问题的优化
 *
 * @author brucewuu
 * @date 2022/3/13 上午10:48
 */
public final class SystemClock {
    /**
     * 设置周期
     */
    private final long period;

    /**
     * 用来作为时间戳存储的容器
     */
    private final AtomicLong now;

    /**
     * 获取毫秒时间戳 替换System.currentTimeMillis()
     */
    public static long currentTimeMillis() {
        return getInstance().now();
    }

    /**
     * 获取当前日期字符串
     * Returns: a String object in yyyy-MM-dd HH:mm:ss.SSS format
     */
    public static String currentTimeToString(long milliseconds) {
        String timestamp = new Timestamp(milliseconds).toString();
        if (timestamp.length() == 23) {
            return timestamp;
        }
        if (timestamp.length() == 22) {
            return timestamp + "0";
        }
        if (timestamp.length() == 21) {
            return timestamp + "00";
        }
        if (timestamp.length() == 19) {
            return timestamp + ".000";
        }
        if (timestamp.length() < 19) {
            return DateUtils.formatDateTime(DateUtils.toDateTime(milliseconds)) + ".000";
        }
        if (timestamp.length() > 23) {
            return timestamp.substring(0, 23);
        }
        return timestamp;
    }

    /**
     * 获取当前时间字符串
     * Returns: a String object in yyyy-MM-dd HH:mm:ss.SSS format
     */
    public static String currentTimeToString() {
        return currentTimeToString(getInstance().now());
    }

    public SystemClock(long period) {
        this.period = period;
        this.now = new AtomicLong(System.currentTimeMillis());
        this.scheduleClockUpdating();
    }

    private static SystemClock getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private long now() {
        return now.get();
    }

    private static class InstanceHolder {
        // 设置1ms更新一次时间
        private static final SystemClock INSTANCE = new SystemClock(1);
    }

    /**
     * 初始化定时器
     */
    private void scheduleClockUpdating() {
        ScheduledExecutorService singleScheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "System Clock");
            //设置为守护线程
            thread.setDaemon(true);
            return thread;
        });
        singleScheduler.scheduleAtFixedRate(() -> now.set(System.currentTimeMillis()), period, period, TimeUnit.MILLISECONDS);
    }

}
