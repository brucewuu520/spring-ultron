package org.springultron.crypto;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;

/**
 * @author brucewuu
 * @date 2020/3/20 10:28
 */
public class RSATests {

    @Test
    public void generateKeyTest() {
        KeyPair keyPair = RSA.generateKeyPair(2048);
        Assert.assertNotNull(keyPair);
        String publicKey = RSA.getPublicKey(keyPair);
        System.out.println("publicKey：" + publicKey);
        String privateKey = RSA.getPrivateKey(keyPair);
        System.out.println("privateKey：" + privateKey);
    }

    @Test
    public void cryptoTest() {
        GlobalBouncyCastleProvider.setUseBouncyCastle(true);

        KeyPair keyPair = RSA.generateKeyPair(2048);
        Assert.assertNotNull(keyPair);
        String publicKey = RSA.getPublicKey(keyPair);
        System.out.println("publicKey：" + publicKey);
        String privateKey = RSA.getPrivateKey(keyPair);
        System.out.println("privateKey：" + privateKey);

        String data = "{春天里那个百花香，:我和妹妹把手牵:}";
        StringBuilder sb = new StringBuilder("--start--");
        for (int i = 0; i < 20; i++) {
            sb.append(data);
        }
        sb.append("--end--");
        data = sb.toString();

        RsaAlgorithms algorithms = RsaAlgorithms.RSA_NONE;
        String encryptData = RSA.encryptByPrivateKey(algorithms, data, privateKey);
        System.err.println("加密后：" + encryptData);
        System.err.println("解密后：" + RSA.decryptByByPublicKey(algorithms, encryptData, publicKey));
    }

    @Test
    public void signTest() {
        KeyPair keyPair = RSA.generateKeyPair(2048);
        Assert.assertNotNull(keyPair);

        RsaAlgorithms algorithms = RsaAlgorithms.RSA_SHA256;
        final String data = "{春天里那个百花香，:我和妹妹把手牵:}";
        byte[] dateBytes = data.getBytes(StandardCharsets.UTF_8);
        byte[] signBytes = RSA.sign(algorithms, dateBytes, keyPair.getPrivate());
        Assert.assertTrue("验签结果：", RSA.verify(algorithms, dateBytes, keyPair.getPublic(), signBytes));
    }


}
