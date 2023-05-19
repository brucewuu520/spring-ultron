package org.springultron.core.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Base64 工具，默认UTF-8编码
 *
 * @author brucewuu
 * @date 2019-06-10 16:10
 */
public class Base64Utils {

    private Base64Utils() {}

    public static byte[] encode(byte[] src) {
        return src.length == 0 ? src : Base64.getEncoder().encode(src);
    }

    public static byte[] decode(byte[] src) {
        return src.length == 0 ? src : Base64.getDecoder().decode(src);
    }

    public static byte[] encodeUrlSafe(byte[] src) {
        return src.length == 0 ? src : Base64.getUrlEncoder().encode(src);
    }

    public static byte[] decodeUrlSafe(byte[] src) {
        return src.length == 0 ? src : Base64.getUrlDecoder().decode(src);
    }

    public static String encodeToString(byte[] src) {
        return src.length == 0 ? "" : Base64.getEncoder().encodeToString(src);
    }

    public static byte[] decodeFromString(String src) {
        return src.isEmpty() ? new byte[0] : Base64.getDecoder().decode(src);
    }

    public static String encodeToUrlSafeString(byte[] src) {
        return Base64.getUrlEncoder().encodeToString(src);
    }

    public static byte[] decodeFromUrlSafeString(String src) {
        return Base64.getUrlDecoder().decode(src);
    }

    public static String encode(String src) {
        return encode(src, StandardCharsets.UTF_8);
    }

    public static String encode(String src, Charset charset) {
        return null == src ? null : new String(encode(src.getBytes(charset)), charset);
    }

    public static String decode(String src) {
        return decode(src, StandardCharsets.UTF_8);
    }

    public static String decode(String src, Charset charset) {
        return null == src ? null : new String(decode(src.getBytes(charset)), charset);
    }

    public static String encodeUrlSafe(String src) {
        return encodeUrlSafe(src, StandardCharsets.UTF_8);
    }

    public static String encodeUrlSafe(String src, Charset charset) {
        return null == src ? null : new String(encodeUrlSafe(src.getBytes(charset)), charset);
    }

    public static String decodeUrlSafe(String src) {
        return decodeUrlSafe(src, StandardCharsets.UTF_8);
    }

    public static String decodeUrlSafe(String src, Charset charset) {
        return null == src ? null : new String(decodeUrlSafe(src.getBytes(charset)), charset);
    }
}
