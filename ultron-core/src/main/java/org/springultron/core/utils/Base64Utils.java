package org.springultron.core.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Base64 工具，默认UTF-8编码
 *
 * @author brucewuu
 * @date 2019-06-10 16:10
 */
public class Base64Utils extends org.springframework.util.Base64Utils {

    private Base64Utils() {}

    public static String encode(String src) {
        return encode(src, StandardCharsets.UTF_8);
    }

    public static String encode(String src, Charset charset) {
        return null == src ? null : new String(Base64Utils.encode(src.getBytes(charset)), charset);
    }

    public static String decode(String src) {
        return decode(src, StandardCharsets.UTF_8);
    }

    public static String decode(String src, Charset charset) {
        return null == src ? null : new String(Base64Utils.decode(src.getBytes(charset)), charset);
    }

    public static String encodeUrlSafe(String src) {
        return encodeUrlSafe(src, StandardCharsets.UTF_8);
    }

    public static String encodeUrlSafe(String src, Charset charset) {
        return null == src ? null : new String(Base64Utils.encodeUrlSafe(src.getBytes(charset)), charset);
    }

    public static String decodeUrlSafe(String src) {
        return decodeUrlSafe(src, StandardCharsets.UTF_8);
    }

    public static String decodeUrlSafe(String src, Charset charset) {
        return null == src ? null : new String(Base64Utils.decodeUrlSafe(src.getBytes(charset)), charset);
    }
}
