package org.springultron.crypto;

import org.junit.Test;

/**
 * @author brucewuu
 * @date 2020/3/20 22:10
 */
public class DESTests {

    @Test
    public void generateKeyTest() {
        String key = DES.generateKey();
        System.out.println("秘钥：" + key);
    }

    @Test
    public void cryptoTest() {
        String key = DES.generateKey();
        System.out.println("秘钥：" + key);

        String data = "{春天里那个百花香，:我和妹妹把手牵:}";
        StringBuilder sb = new StringBuilder("--start--");
        for (int i = 0; i < 20; i++) {
            sb.append(data);
        }
        sb.append("--end--");
        data = sb.toString();

        String encryptData = DES.encryptHex(data, key);
        System.out.println("加密后：" + encryptData);
        System.out.println("解密后：" + DES.decrypt(encryptData, key));
    }
}
