package org.springultron.crypto;

import org.junit.Test;
import org.springframework.util.Base64Utils;
import org.springultron.core.utils.Hex;

/**
 * @author brucewuu
 * @date 2020/3/20 18:50
 */
public class AESTests {

    @Test
    public void generateKeyTest() {
        byte[] keyBytes = AES.generateKey(128);
        System.out.println("秘钥base64：" + Base64Utils.encodeToString(keyBytes));
        System.out.println("秘钥Hex：" + Hex.encodeHexString(keyBytes));
    }

    @Test
    public void cryptoTest() {
        final String data = "{春天里那个百花香，:我和妹妹把手牵:}";
        String key = AES.generateKeyBase64(256);
        AesAlgorithms algorithms = AesAlgorithms.AES_ECB_PKCS5;
//        String key = RandomUtils.random(32);
        System.err.println("秘钥：" + key);
        String encryptData = AES.encrypt(algorithms, data, key);
        System.err.println("加密后：" + encryptData);
        System.err.println("解密后：" + AES.decrypt(algorithms, encryptData, key));

        encryptData = AES.encryptHex(algorithms, data, key);
        System.err.println("加密后22：" + encryptData);
        System.err.println("解密后22：" + AES.decrypt(algorithms, encryptData, key));
    }
}
