package org.springultron.core.utils;

import org.springframework.util.Assert;

import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机数工具
 *
 * @author brucewuu
 * @date 2019/10/28 12:08
 */
public class RandomUtils {

    private RandomUtils() {
    }

    /**
     * 随机数
     */
    private static final String S_INT;
    private static final String S_STR;
    private static final String S_ALL;

    static {
        S_INT = "0123456789";
        S_STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        S_ALL = S_STR + S_INT;
    }

    /**
     * 获取随机数生成器对象
     * ThreadLocalRandom是JDK 7之后提供并发产生随机数，能够解决多个线程发生的竞争争夺。
     *
     * @return {@link ThreadLocalRandom}
     */
    public static ThreadLocalRandom getRandom() {
        return ThreadLocalRandom.current();
    }

    /**
     * 创建{@link SecureRandom}，类提供加密的强随机数生成器 (RNG)<br>
     *
     * @param seed 自定义随机种子
     * @return {@link SecureRandom}
     */
    public static SecureRandom createSecureRandom(byte[] seed) {
        return (null == seed) ? new SecureRandom() : new SecureRandom(seed);
    }

    /**
     * 生成随机的字符串
     *
     * @param count 字符穿长度
     * @return 随机数
     */
    public static String random(int count) {
        return random(count, RandomType.ALL);
    }

    /**
     * 随机数生成
     *
     * @param count      字符长度
     * @param randomType 随机数类别
     * @return 随机数
     */
    public static String random(int count, RandomType randomType) {
        Assert.isTrue(count > 0, "Requested random string length " + count + " is less than 0.");
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        char[] buffer = new char[count];
        for (int i = 0; i < count; i++) {
            if (RandomType.INT == randomType) {
                buffer[i] = S_INT.charAt(random.nextInt(S_INT.length()));
            } else if (RandomType.STRING == randomType) {
                buffer[i] = S_STR.charAt(random.nextInt(S_STR.length()));
            } else {
                buffer[i] = S_ALL.charAt(random.nextInt(S_ALL.length()));
            }
        }
        return new String(buffer);
    }

    /**
     * 获得随机Boolean值
     *
     * @return true or false
     */
    public static boolean randomBoolean() {
        return 0 == randomInt(2);
    }

    /**
     * 获得随机数[0, 2^32)
     *
     * @return 随机数
     */
    public static int randomInt() {
        return getRandom().nextInt();
    }

    /**
     * 获得指定范围内的随机数 [0,limit)
     *
     * @param limit 限制随机数的范围，不包括这个数
     * @return 随机数
     */
    public static int randomInt(int limit) {
        return getRandom().nextInt(limit);
    }

    /**
     * 获得指定范围内的随机数
     *
     * @param min 最小数（包含）
     * @param max 最大数（不包含）
     * @return 随机数
     */
    public static int randomInt(int min, int max) {
        return getRandom().nextInt(min, max);
    }

    /**
     * 获得随机数
     *
     * @return 随机数
     */
    public static long randomLong() {
        return getRandom().nextLong();
    }

    /**
     * 获得指定范围内的随机数 [0,limit)
     *
     * @param limit 限制随机数的范围，不包括这个数
     * @return 随机数
     */
    public static long randomLong(long limit) {
        return getRandom().nextLong(limit);
    }

    /**
     * 获得指定范围内的随机数[min, max)
     *
     * @param min 最小数（包含）
     * @param max 最大数（不包含）
     * @return 随机数
     */
    public static long randomLong(long min, long max) {
        return getRandom().nextLong(min, max);
    }

    /**
     * 获得随机数[0, 1)
     *
     * @return 随机数
     */
    public static double randomDouble() {
        return getRandom().nextDouble();
    }

    /**
     * 获得指定范围内的随机数 [0,limit)
     *
     * @param limit 限制随机数的范围，不包括这个数
     * @return 随机数
     */
    public static double randomDouble(double limit) {
        return getRandom().nextDouble(limit);
    }

    /**
     * 获得指定范围内的随机数
     *
     * @param min 最小数（包含）
     * @param max 最大数（不包含）
     * @return 随机数
     */
    public static double randomDouble(double min, double max) {
        return getRandom().nextDouble(min, max);
    }

    /**
     * 获得指定范围内的随机数
     *
     * @param scale        保留小数位数
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 随机数
     */
    public static double randomDouble(int scale, RoundingMode roundingMode) {
        return NumberUtils.round(randomDouble(), scale, roundingMode).doubleValue();
    }

    /**
     * 获得指定范围内的随机数
     *
     * @param min          最小数（包含）
     * @param max          最大数（不包含）
     * @param scale        保留小数位数
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 随机数
     */
    public static double randomDouble(double min, double max, int scale, RoundingMode roundingMode) {
        return NumberUtils.round(randomDouble(min, max), scale, roundingMode).doubleValue();
    }

    /**
     * 随机byte数组
     *
     * @param length 长度
     * @return bytes
     */
    public static byte[] randomBytes(int length) {
        byte[] bytes = new byte[length];
        getRandom().nextBytes(bytes);
        return bytes;
    }
}