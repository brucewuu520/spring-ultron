package org.springultron.core.utils;

import java.nio.charset.StandardCharsets;

/**
 * Base64 工具
 *
 * @Auther: brucewuu
 * @Date: 2019-06-10 16:10
 * @Description:
 */
public class Base64Utils extends org.springframework.util.Base64Utils {

    private Base64Utils() {}

    public static String encodeToString(String src) {
        return Base64Utils.encodeToString(src.getBytes(StandardCharsets.UTF_8));
    }

    public static String encodeToUrlSafeString(String src) {
        return Base64Utils.encodeToUrlSafeString(src.getBytes(StandardCharsets.UTF_8));
    }
}
