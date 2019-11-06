package org.springultron.core.crypto;

import org.springultron.core.utils.Base64Utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * AES加密解密算法实现
 * 高级加密标准（英语：Advanced Encryption Standard，缩写：AES）
 * 对于Java中AES的默认模式是：AES/ECB/PKCS5Padding，如果使用CryptoJS，请调整为：padding: CryptoJS.pad.Pkcs7
 *
 * @author brucewuu
 * @date 2019/10/28 11:38
 */
public class AES {

    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM_ECB = "AES/ECB/PKCS5Padding";
    private static final String CIPHER_ALGORITHM_CBC = "AES/CBC/NoPadding";

    private AES() {
    }

    /**
     * 加密
     *
     * @param data 待加密字符串
     * @param key  key base64
     * @return 加密结果 base64
     */
    public static String encrypt(String data, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        return Base64Utils.encodeToString(encrypt(dataBytes, key));
    }

    /**
     * 加密
     *
     * @param data 待加密数据
     * @param key  key base64
     */
    public static byte[] encrypt(byte[] data, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey(key));
        return cipher.doFinal(data);
    }

    /**
     * 解密
     *
     * @param data 已加密字符串 base64
     * @param key  key base64
     * @return 解密结果
     */
    public static String decrypt(String data, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] dataBytes = Base64Utils.decodeFromString(data);
        return new String(decrypt(dataBytes, key), StandardCharsets.UTF_8);
    }

    /**
     * 解密
     *
     * @param data 加密数据
     * @param key  key base64
     */
    public static byte[] decrypt(byte[] data, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
        cipher.init(Cipher.DECRYPT_MODE, secretKey(key));
        return cipher.doFinal(data);
    }

    private static SecretKey secretKey(final String key) {
        byte[] keyBytes = Base64Utils.decodeFromString(key);
        return new SecretKeySpec(keyBytes, KEY_ALGORITHM);
    }

    /**
     * 生成AES加密秘钥
     * <p>
     * 192、256位秘钥需下载并安装JCE无限制权限策略文件
     * https://www.cnblogs.com/mrjade/p/10886378.html
     *
     * @param length 位数，可以是 128、192、256、越大越安全
     * @return key base64
     */
    public static String generateKey(final int length) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(length);
        byte[] keyBytes = keyGenerator.generateKey().getEncoded();
        return Base64Utils.encodeToString(keyBytes);
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
            int pad = (int) decrypted[decrypted.length - 1];
            if (pad < 1 || pad > BLOCK_SIZE) {
                pad = 0;
            }
            if (pad > 0) {
                return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
            }
            return decrypted;
        }
    }

    public static void main(String[] args) throws Exception {
//        LocalDate localDate = LocalDate.now();
        // 本月份
//        int month = localDate.getMonthValue();
//        System.err.println(localDate.getDayOfWeek().getValue());
//        System.err.println(generateKey(256));
//        String key = "NJUfll7QQuWbJ/BNggSzgwkYzlGgbwuI";
//        String str = "ak47ijg";
//        System.err.println(encrypt(str, key));
//        System.err.println(decrypt("str", ));
        System.err.println(String.join("|", "x987", "fdlk", "1321d"));
    }

}
