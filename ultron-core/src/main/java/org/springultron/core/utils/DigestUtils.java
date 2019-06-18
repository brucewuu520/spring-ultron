package org.springultron.core.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具封装
 *
 * @Auther: brucewuu
 * @Date: 2019-06-06 10:44
 * @Description:
 */
public class DigestUtils extends org.springframework.util.DigestUtils {

    /**
     * Calculates the MD5 digest and returns the value as a 32 character hex string.
     *
     * @param data Data to digest
     * @return MD5 digest as a hex string
     */
    public static String md5Hex(String data) {
        return DigestUtils.md5DigestAsHex(data.getBytes(StandardCharsets.UTF_8));
    }

    public static String sha1Hex(String srcStr) {
        return hashHex("SHA-1", srcStr);
    }

    public static String sha256Hex(String srcStr) {
        return hashHex("SHA-256", srcStr);
    }

    public static String sha384Hex(String srcStr) {
        return hashHex("SHA-384", srcStr);
    }

    public static String sha512Hex(String srcStr) {
        return hashHex("SHA-512", srcStr);
    }

    public static String hashHex(final String algorithm, final String srcStr) {
        byte[] bytes = getDigest(algorithm).digest(srcStr.getBytes(StandardCharsets.UTF_8));
        return Hex.encodeHexString(bytes);
    }

    private static MessageDigest getDigest(final String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalStateException("Could not find MessageDigest with algorithm \"" + algorithm + "\"", e);
        }
    }

    /**
     * 自定义加密 先MD5再SHA1
     *
     * @param data 数据
     * @return String
     */
    public static String encrypt(final String data) {
        return sha1Hex(md5Hex(data));
    }
}
