package org.springultron.core.crypto;

import org.springframework.util.Assert;
import org.springultron.core.exception.Exceptions;
import org.springultron.core.utils.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * AES加密/解密算法实现
 * 高级加密标准（英语：Advanced Encryption Standard，缩写：AES）
 * 对于Java中AES的默认模式是：AES/ECB/PKCS5Padding，如果使用CryptoJS，请调整为：padding: CryptoJS.pad.Pkcs7
 *
 * @author brucewuu
 * @date 2019/10/28 11:38
 */
public final class AES {

    private AES() {
    }

    private static final String KEY_ALGORITHM = "AES";

    /**
     * 生成AES加密秘钥（base64编码）
     * <p>
     * 192、256位秘钥需下载并安装JCE无限制权限策略文件
     * https://www.cnblogs.com/mrjade/p/10886378.html
     * </p>
     *
     * @param length 位数，可以是 128(24位)、192(32位)、256(44) 越大越安全
     * @return key base64
     */
    public static String generateKey(final int length) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(length);
        byte[] keyBytes = keyGenerator.generateKey().getEncoded();
        return Base64Utils.encodeToString(keyBytes);
    }

    /**
     * 加密，默认使用：AES/ECB/PKCS5Padding 算法
     *
     * @param data 待加密字符串
     * @param key  key
     * @return 加密结果 base64
     */
    public static String encrypt(String data, String key) {
        return encrypt(AesAlgorithms.AES_ECB_PKCS5, data, key);
    }

    /**
     * 解密，默认使用：AES/ECB/PKCS5Padding 算法
     *
     * @param data 已加密字符串 base64
     * @param key  key
     * @return 解密结果
     */
    public static String decrypt(String data, String key) {
        return decrypt(AesAlgorithms.AES_ECB_PKCS5, data, key);
    }

    /**
     * 加密
     *
     * @param algorithms 算法
     * @param data       待加密字符串
     * @param key        秘钥
     * @return 加密后字符串 base64
     */
    public static String encrypt(AesAlgorithms algorithms, String data, String key) {
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes;
        if (algorithms == AesAlgorithms.AES_ECB_PKCS5) {
            keyBytes = Base64Utils.decodeFromString(key);
        } else {
            keyBytes = key.getBytes(StandardCharsets.UTF_8);
        }
        byte[] encryptBytes = encrypt(algorithms, dataBytes, keyBytes);
        return Base64Utils.encodeToString(encryptBytes);
    }

    /**
     * 解密
     *
     * @param algorithms 算法
     * @param data       待解密字符串 base64
     * @param key        秘钥
     * @return 解密后文本
     */
    public static String decrypt(AesAlgorithms algorithms, String data, String key) {
        byte[] dataBytes = Base64Utils.decodeFromString(data);
        byte[] keyBytes;
        if (algorithms == AesAlgorithms.AES_ECB_PKCS5) {
            keyBytes = Base64Utils.decodeFromString(key);
        } else {
            keyBytes = key.getBytes(StandardCharsets.UTF_8);
        }
        byte[] decryptBytes = decrypt(algorithms, dataBytes, keyBytes);
        return new String(decryptBytes, StandardCharsets.UTF_8);
    }

    /**
     * 加密
     *
     * @param algorithms 算法
     * @param data       待加密文本字节数组
     * @param key        秘钥字节数组
     * @return 加密后的字节数组
     */
    public static byte[] encrypt(AesAlgorithms algorithms, byte[] data, byte[] key) {
        if (algorithms == AesAlgorithms.AES_CBC_PKCS7) {
            Assert.isTrue(key.length == 32, "IllegalAesKey, aesKey's length must be 32");
        }
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(algorithms.getValue());
            if (algorithms == AesAlgorithms.AES_ECB_PKCS5) {
                cipher.init(Cipher.ENCRYPT_MODE, keySpec);
                return cipher.doFinal(data);
            } else {
                IvParameterSpec iv = new IvParameterSpec(key, 0, 16);
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
                return cipher.doFinal(Pkcs7Encoder.encode(data));
            }
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 解密
     *
     * @param algorithms 算法
     * @param data       待解密文本字节数组
     * @param key        秘钥字节数组
     * @return 解密后的字节数组
     */
    public static byte[] decrypt(AesAlgorithms algorithms, byte[] data, byte[] key) {
        if (algorithms == AesAlgorithms.AES_CBC_PKCS7) {
            Assert.isTrue(key.length == 32, "IllegalAesKey, aesKey's length must be 32");
        }
        try {
            Cipher cipher = Cipher.getInstance(algorithms.getValue());
            SecretKeySpec keySpec = new SecretKeySpec(key, KEY_ALGORITHM);
            if (algorithms == AesAlgorithms.AES_ECB_PKCS5) {
                cipher.init(Cipher.DECRYPT_MODE, keySpec);
                return cipher.doFinal(data);
            } else {
                IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(key, 0, 16));
                cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
                return Pkcs7Encoder.decode(cipher.doFinal(data));
            }
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 提供基于PKCS7算法的加解密接口.
     */
    private static class Pkcs7Encoder {
        private static int BLOCK_SIZE = 32;

        private static byte[] encode(byte[] src) {
            int count = src.length;
            // 计算需要填充的位数
            int amountToPad = BLOCK_SIZE - (count % BLOCK_SIZE);
            if (amountToPad == 0) {
                amountToPad = BLOCK_SIZE;
            }
            // 获得补位所用的字符
            byte pad = (byte) (amountToPad & 0xFF);
            byte[] pads = new byte[amountToPad];
            for (int index = 0; index < amountToPad; index++) {
                pads[index] = pad;
            }
            int length = count + amountToPad;
            byte[] dest = new byte[length];
            System.arraycopy(src, 0, dest, 0, count);
            System.arraycopy(pads, 0, dest, count, amountToPad);
            return dest;
        }

        private static byte[] decode(byte[] decrypted) {
            int pad = decrypted[decrypted.length - 1];
            if (pad < 1 || pad > BLOCK_SIZE) {
                pad = 0;
            }
            if (pad > 0) {
                return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
            }
            return decrypted;
        }
    }

//    public static void main(String[] args) {
////        System.err.println(generateKey(192));
//        String key = "nvJ0hh3EbeD/CoYLKVA6W8McjrXKYLOl";
////        String key = RandomUtils.random(32);
//        String str = "春天里那个百花香，我和妹妹把手牵";
//        str = encrypt(AesAlgorithms.AES_CBC_PKCS7, str, key);
//        System.err.println("加密后:" + str);
//        System.err.println("解密后:" + decrypt(AesAlgorithms.AES_CBC_PKCS7, str, key));
//    }

}
