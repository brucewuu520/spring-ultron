package org.springultron.crypto;

import org.springframework.util.Base64Utils;
import org.springultron.core.exception.CryptoException;
import org.springultron.core.utils.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;

/**
 * 国密SM4算法实现，基于BC库
 *
 * <p>
 * 对称加密，无线局域网标准的分组数据算法。
 * </p>
 *
 * @author brucewuu
 * @date 2020/3/19 21:16
 */
public class SM4 {

    private SM4() {
    }

    private static final String ALGORITHM_NAME = "SM4";

    /**
     * 生成秘钥（base64）
     *
     * @return 秘钥字符串 base64
     */
    public static String generateKeyBase64() {
        return Base64Utils.encodeToString(generateKey());
    }

    /**
     * 生成秘钥（Hex 16进制编码）
     *
     * @return 秘钥字符串 Hex 16进制编码
     */
    public static String generateKeyHex() {
        return Hex.encodeHexString(generateKey());
    }

    /**
     * 生成秘钥
     *
     * @return 秘钥 bytes
     */
    public static byte[] generateKey() {
        SecretKey secretKey = SecureUtils.generateKey(ALGORITHM_NAME);
        return secretKey.getEncoded();
    }

    /**
     * 加密（base64）
     *
     * @param data 待加密数据
     * @param key  秘钥
     * @return 加密后的字符串 base64
     */
    public static String encrypt(String data, String key) {
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = SecureUtils.decode(key);
        byte[] encryptBytes = encrypt(dataBytes, keyBytes);
        return Base64Utils.encodeToString(encryptBytes);
    }

    /**
     * 加密
     *
     * @param data 待加密数据
     * @param key  秘钥
     * @return 加密后的字节数组
     */
    public static byte[] encrypt(byte[] data, byte[] key) {
        SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM_NAME);
        final Cipher cipher = SecureUtils.createCipher(ALGORITHM_NAME);
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return cipher.doFinal(data);
        } catch (final InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new CryptoException(e);
        }
    }

    /**
     * 加密（Hex 16进制编码）
     *
     * @param data 待加密数据
     * @param key  秘钥
     * @return 加密后的字符串 Hex 16进制编码
     */
    public static String encryptHex(String data, String key) {
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = SecureUtils.decode(key);
        byte[] encryptBytes = encrypt(dataBytes, keyBytes);
        return Hex.encodeHexString(encryptBytes);
    }

    /**
     * 解密
     *
     * @param data 密文数据
     * @param key  秘钥
     * @return 解密后的数据
     */
    public static String decrypt(String data, String key) {
        byte[] dataBytes = SecureUtils.decode(data);
        byte[] keyBytes = SecureUtils.decode(key);
        byte[] decryptBytes = decrypt(dataBytes, keyBytes);
        return new String(decryptBytes, StandardCharsets.UTF_8);
    }

    /**
     * 解密
     *
     * @param data 密文数据
     * @param key  秘钥
     * @return 解密后的字节数组
     */
    public static byte[] decrypt(byte[] data, byte[] key) {
        SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM_NAME);
        final Cipher cipher = SecureUtils.createCipher(ALGORITHM_NAME);
        try {
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            return cipher.doFinal(data);
        } catch (final InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new CryptoException(e);
        }
    }
}
