package org.springultron.core.crypto;

import org.springframework.util.Base64Utils;
import org.springultron.core.exception.Exceptions;
import org.springultron.core.utils.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

/**
 * DES加密/解密算法实现
 * <p>
 * DES全称为Data Encryption Standard，即数据加密标准，是一种使用密钥加密的块算法
 * DES加密和解密过程中，密钥长度都必须是8的倍数
 * Java中默认实现为：DES/CBC/PKCS5Padding
 * </p>
 *
 * @author brucewuu
 * @date 2019/11/17 16:04
 */
public final class DES {
    private DES() {
    }

    private static final String KEY_ALGORITHM = "DES";

    /**
     * 生成秘钥，密钥长度都必须是8的倍数
     * <p>
     * 也可自定义生成只要是8的倍数即可 RandomUtils.random(32)
     * </p>
     *
     * @return 16为长度的秘钥
     */
    public static String generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(56);
        byte[] keyBytes = keyGenerator.generateKey().getEncoded();
        return Hex.encodeHexString(keyBytes);
    }

    /**
     * 加密
     *
     * @param data 待加密文本
     * @param key  秘钥
     * @return 加密后字符串 base64
     */
    public static String encrypt(String data, String key) {
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] encryptBytes = encrypt(dataBytes, keyBytes);
        return Base64Utils.encodeToString(encryptBytes);
    }

    /**
     * 解密
     *
     * @param data 密文 base64
     * @param key  秘钥
     */
    public static String decrypt(String data, String key) {
        byte[] dataBytes = Base64Utils.decodeFromString(data);
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] decryptBytes = decrypt(dataBytes, keyBytes);
        return new String(decryptBytes, StandardCharsets.UTF_8);
    }

    /**
     * 加密
     *
     * @param data 待加密数据字节数组
     * @param key  秘钥
     */
    public static byte[] encrypt(byte[] data, byte[] key) {
        try {
            DESKeySpec keySpec = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
            SecretKey secretKey = keyFactory.generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 解密
     *
     * @param data 密文字节数组
     * @param key  秘钥
     */
    public static byte[] decrypt(byte[] data, byte[] key) {
        try {
            DESKeySpec keySpec = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
            SecretKey secretKey = keyFactory.generateSecret(keySpec);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }
}