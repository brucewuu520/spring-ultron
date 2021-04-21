package org.springultron.wechat.encrypt;

import org.springultron.core.utils.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Key;

/**
 * 微信小程序-加密数据解密算法
 *
 * @author brucewuu
 * @date 2021/4/19 下午6:11
 */
public class WxaBizDataCrypt {

    /**
     * AES解密
     * @param encryptedData 密文
     * @param iv iv
     * @return {String}
     */
    public static String decrypt(String encryptedData, String sessionKey, String iv) {
        byte[] bizData = Base64Utils.decodeFromString(encryptedData);
        byte[] keyByte = Base64Utils.decodeFromString(sessionKey);
        byte[] ivByte  = Base64Utils.decodeFromString(iv);
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            Key keySpec = new SecretKeySpec(keyByte, "AES");
            // 初始化
            AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
            params.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, keySpec, params);
            byte[] original = cipher.doFinal(bizData);
            // 去除补位字符
            byte[] result = PKCS7Encoder.decode(original);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("aes解密失败");
        }
    }
}
