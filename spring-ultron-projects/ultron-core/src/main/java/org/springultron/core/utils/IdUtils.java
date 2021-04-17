package org.springultron.core.utils;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 唯一性ID生成器
 *
 * @author brucewuu
 * @date 2019/10/27 21:44
 */
public final class IdUtils {

    private IdUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取随机UUID（去掉了横线）
     *
     * @return UUID
     */
    public static String randomUUID() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return new UUID(random.nextLong(), random.nextLong()).toString().replace("-", "");
    }

    private static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmssSS");

    private static final AtomicInteger SEQ = new AtomicInteger(1000);

    /**
     * 生成订单号
     * 支持分布式订单号不重复
     *
     * @return 20位订单号
     */
    public static String genOrderNo() {
        if (SEQ.intValue() > 9990) {
            SEQ.getAndSet(1000);
        }
        return DT_FORMATTER.format(LocalDateTime.now()) + getLocalIpSuffix() + SEQ.getAndIncrement();
    }

    private volatile static String IP_SUFFIX = null;

    private static String getLocalIpSuffix() {
        if (null != IP_SUFFIX) {
            return IP_SUFFIX;
        }
        try {
            synchronized (IdUtils.class) {
                if (null != IP_SUFFIX) {
                    return IP_SUFFIX;
                }
                InetAddress addr = InetAddress.getLocalHost();
                //  172.17.0.4  172.17.0.199,
                String hostAddress = addr.getHostAddress();
                if (null != hostAddress && hostAddress.length() > 4) {
                    String ipSuffix = hostAddress.trim().split("\\.")[3];
                    if (ipSuffix.length() == 2) {
                        IP_SUFFIX = ipSuffix;
                        return IP_SUFFIX;
                    }
                    ipSuffix = "0" + ipSuffix;
                    IP_SUFFIX = ipSuffix.substring(ipSuffix.length() - 2);
                    return IP_SUFFIX;
                }
                IP_SUFFIX = RandomUtils.random(2, RandomType.INT);
                return IP_SUFFIX;
            }
        } catch (Exception e) {
            System.out.println("获取IP失败: " + e.getMessage());
            IP_SUFFIX = RandomUtils.random(2, RandomType.INT);
            return IP_SUFFIX;
        }
    }
}