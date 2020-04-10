package org.springultron.core.utils;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 唯一性ID生成器
 *
 * @author brucewuu
 * @date 2019/10/27 21:44
 */
public class IdUtils {

    private IdUtils() {
    }

    /**
     * 获取随机UUID（去掉了横线）
     *
     * @return UUID
     */
    public static String randomUUID() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return new UUID(random.nextLong(), random.nextLong()).toString().replaceAll("-", "");
    }
}