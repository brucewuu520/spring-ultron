package org.springultron.crypto;

import org.junit.Assert;
import org.junit.Test;
import org.springultron.core.utils.RandomUtils;

import java.security.KeyPair;

/**
 * @author brucewuu
 * @date 2020/3/20 11:54
 */
public class SM2Test {

    @Test
    public void generateKeyPairTest() {
        System.out.println(RandomUtils.random(16));
        KeyPair keyPair = SM2.generateKeyPair(2048);
        Assert.assertNotNull(keyPair);
        String publicKey = SM2.getPublicKey(keyPair);
        System.out.println("publicKey：" + publicKey);
        String privateKey = SM2.getPrivateKey(keyPair);
        System.out.println("privateKey：" + privateKey);
    }

    @Test
    public void cryptoTest() {
        String data = "{春天里那个百花香，:我和妹妹把手牵:}";
        StringBuilder sb = new StringBuilder("--start--");
        for (int i = 0; i < 20; i++) {
            sb.append(data);
        }
        sb.append("--end--");
        data = sb.toString();

        KeyPair keyPair = SM2.generateKeyPair(2048);
        Assert.assertNotNull(keyPair);
        String publicKey = SM2.getPublicKey(keyPair);
        System.out.println("publicKey：" + publicKey);
        String privateKey = SM2.getPrivateKey(keyPair);
        System.out.println("privateKey：" + privateKey);

        String encryptData = SM2.encrypt(data, publicKey);
        System.err.println("加密后：" + encryptData);
        System.err.println("解密后：" + SM2.decrypt(encryptData, privateKey));
    }

    @Test
    public void signTest() {
        String data = "{春天里那个百花香，:我和妹妹把手牵:}";
        StringBuilder sb = new StringBuilder("--start--");
        for (int i = 0; i < 20; i++) {
            sb.append(data);
        }
        sb.append("--end--");
        data = sb.toString();
        final String id = RandomUtils.random(8);

        KeyPair keyPair = SM2.generateKeyPair(2048);
        Assert.assertNotNull(keyPair);
        String publicKey = SM2.getPublicKey(keyPair);
        System.out.println("publicKey：" + publicKey);
        String privateKey = SM2.getPrivateKey(keyPair);
        System.out.println("privateKey：" + privateKey);

        String sign = SM2.sign(data, privateKey, id);
        System.err.println("签名后：" + sign);
        System.err.println("验签结果：" + SM2.verify(data, publicKey, sign, id));
    }

}
