package org.springultron.crypto;

import org.junit.Test;
import org.springframework.util.Base64Utils;
import org.springultron.core.utils.Hex;

import java.nio.charset.StandardCharsets;

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

    @Test
    public void decryptWechat() {
        String sessionKey = "vkMrQmlmrJkEGW9Curka7g==";
        String encryptedData = "9az8vEfkjog1BdGUDblChpK7tmFTSMvnDaItxZqih9xA1+bEywSbr6b357ZwWYypnTXQvhIEmqgrOZs2FFkFE1NGiSHlLumK+4pbDDt9+vBS0WZh0SuMPNlYtmFT3/sKXmoi6UGZ2Q71JAzM7FilKunFhRZPHj5jjoIUubWu291ufbHqh2dkZytxeZxZRV3fLwcsMFMmWVhXt1W41LFuEg==";
        String iv = "Gv80JaVDpE4RmhbUoRQEDg==";

        byte[] keyBytes = Base64Utils.decodeFromString(sessionKey);
        System.err.println(keyBytes.length);

        byte[] dataBytes = Base64Utils.decodeFromString(encryptedData);
        System.err.println(dataBytes.length);


        // byte[] decryptData = AES.decrypt(AesAlgorithms.AES_CBC_PKCS7, dataBytes, keyBytes, Base64Utils.decodeFromString(iv));
        // System.err.println(decryptData.length);
        //
        //
        // String data = new String(decryptData);

        String data = AES.decrypt(AesAlgorithms.AES_CBC_PKCS7, encryptedData, sessionKey, iv);

        System.err.println(data);
    }
}
