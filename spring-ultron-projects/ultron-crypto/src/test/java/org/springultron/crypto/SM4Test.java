package org.springultron.crypto;

import org.junit.Test;

/**
 * @author brucewuu
 * @date 2020/3/20 21:48
 */
public class SM4Test {

    @Test
    public void generateKeyTest() {
//        byte[] keyBytes = SM4.generateKey();
//        System.out.println("base64秘钥：" + Base64Utils.encodeToString(keyBytes));
//        System.out.println("Hex秘钥：" + Hex.encodeHexString(keyBytes));
        System.out.println("Hex秘钥：" + SM4.generateKeyHex());
    }

    @Test
    public void cryptoTest() {
        SecureUtils.enableBouncyCastle();
        String key = SM4.generateKeyBase64();
//        String key = SM4.generateKeyHex();
        String data = "{春天里那个百花香，:我和妹妹把手牵:}";
        StringBuilder sb = new StringBuilder("--start--");
        for (int i = 0; i < 20; i++) {
            sb.append(data);
        }
        sb.append("--end--");
        data = sb.toString();

        String encryptData = SM4.encrypt(data, key);
        System.out.println("加密后：" + encryptData);
        System.out.println("解密后：" + SM4.decrypt(encryptData, key));
    }
}
