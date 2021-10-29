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
        KeyPair keyPair = RSA.generateKeyPair(1024);
        Assert.assertNotNull(keyPair);
        String publicKey = RSA.getPublicKey(keyPair);
        System.out.println("publicKey：" + publicKey);
        String privateKey = RSA.getPrivateKey(keyPair);
        System.out.println("privateKey：" + privateKey);
    }

    @Test
    public void cryptoTest() {
        GlobalBouncyCastleProvider.setUseBouncyCastle(true);

//        KeyPair keyPair = RSA.generateKeyPair(2048);
//        Assert.assertNotNull(keyPair);
//        String publicKey = RSA.getPublicKey(keyPair);
//        System.out.println("publicKey：" + publicKey);
//        String privateKey = RSA.getPrivateKey(keyPair);
//        System.out.println("privateKey：" + privateKey);

        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ1oDRSqixTerRN1szzlIL+OFkeupWx11ttAaz3oJSVuqa9cls8iLsjF0jg2/qrb5PEyLf/J1QGj/ild5OUVsD33HnM2/aeL92IuoiG6+5fp/jrchJJb1MjS4q4EjRIYrBnEOrjvLNEHTbV6gRdOu0JanFB5F/37/4gTSf8+ec1pAgMBAAECgYAA2aXXoOcGc2Sc8D9Q8u7ECChnH1mFpNWzqqPw/2+t7VRXEebeRo5A555OCfq09IFGmELS+ouij1fMvJM8XVaM5YCT4z8ibFajDhVb+0keV8sRHQYBpfUgsLgrX7j3rBzkGC6bkmU5n6F1HbYgc6Wzi41GnYuYMP4fQuiHk/G4FQJBAM6dBNGoj5wGLA9C9hXZnnt8oDVVrYt9csJOKJYLBhn+ZvxeP2Gs3GoBPnrVcvD0eoEY9s5eEFBcOG/4vqgou9UCQQDDB/zoB+VE3FcHv65fOh5XAPmvMYkObNjlCkIL5no3PPpFfT0R64EBgvJv6MgPRDNhURz1hOFT3UHnhK2hGflFAkEAoUQbq82GcgC7SRo526olMjt/DMIYGAVNmm3I7fwiwg04swiZLhLvY2ofK18JPRuFttgWY9M1ppOklXkwzy4kFQJBAJcHlYsdqicENoUyo9bsi9g2UiSgSJGaKvBbXEAY8CXcm7a6QFOjOxivjgypiFuCJgPDq5hZmaC3I7Uk4sxx770CQB3CUbe9mRQi3sK7GH3pKwdKNOIoaPzw7mEX/t8SkCjHNvqqemr/g6xYlLgyZBZg8yXhqzYbR1KlmseHlTw3m5w=";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCdaA0UqosU3q0TdbM85SC/jhZHrqVsddbbQGs96CUlbqmvXJbPIi7IxdI4Nv6q2+TxMi3/ydUBo/4pXeTlFbA99x5zNv2ni/diLqIhuvuX6f463ISSW9TI0uKuBI0SGKwZxDq47yzRB021eoEXTrtCWpxQeRf9+/+IE0n/PnnNaQIDAQAB";

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
        System.err.println("解密后：" + RSA.decryptByPublicKey(algorithms, encryptData, publicKey));
    }

    @Test
    public void cryptoTest2() {
        GlobalBouncyCastleProvider.setUseBouncyCastle(true);
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnJTgxdT/fnNYyHobo42DGHbr60ZHvgFgaFGt2BbHqKBJJrdrvzcdpuhe6MmdlRcHC/0dOYQNpi1rJqQuZHDawmnxS/o6GHykT9/kMeQr3MpPw5sTWC4gY62pX1S3sF9/ZBgCcE3fcqe0eOCGQ3i4ywYrHjM0KK8GmVJxzClfY7wIDAQAB";
        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKclODF1P9+c1jIehujjYMYduvrRke+AWBoUa3YFseooEkmt2u/Nx2m6F7oyZ2VFwcL/R05hA2mLWsmpC5kcNrCafFL+joYfKRP3+Qx5Cvcyk/DmxNYLiBjralfVLewX39kGAJwTd9yp7R44IZDeLjLBiseMzQorwaZUnHMKV9jvAgMBAAECgYAF42Y+AGqotiyp4rpZW4WWoXSS2IswXT8fxe0AJgOFJfF41Xlo4ZmUEBZkXLHne+V5QtN2kMrSwEXiC+TReWkgrtQGs5hW+K2afRN9EG84QLHBE0/zTj4AGL7QQj/i5lpDw65lHmTUi0bu+vyKRZDqOtSHZhIuyUJWhlPlnyyakQJBAN4YtBrFZKnx5QS6MmwpixV9CSJZ0ezgPQPaitUhmqaEh3ehU/ObMMLUiOygdaWl51gngV68o9vpcHGAc8B7lZ0CQQDAqRWtbGwLipcr9986RsKX/yap8tZUc1TwTBX2OShOCRUoQhdEImxDCDq9ODp+pgiR3ogy52o2RLGXX4Vr+Ej7AkBtRjBPP7HQn/Czi71FVIdXEkcNPK454vaCT+OhC8Cgj3JrnNfGcyPhqFqaOjTIRMF3sTBI4X2ptMibP700BlpNAkBssXgnZVn3kPMEvU1VZYJ3ml3H19cPwhmTkHl7dnfEvt8O3Wqe6ATINKZfBpAJz8ZZ9YBFp8WGZG8FBcESTvttAkBNdtSMw2gp139EppfTtougdkKO1e1M3CkNYRLRTgZ9CWWIrnAk/9Xwa3/KfO1vRpTT9PmMXXgci3nxNDMIp9CV";

        String mobile = "13795273866";

        String encryptData = RSA.encryptByPublicKey(RsaAlgorithms.RSA_NONE_PKCS1, mobile, publicKey);
        System.err.println("加密后：" + encryptData);
        System.err.println("解密后：" + RSA.decryptByPrivateKey(RsaAlgorithms.RSA_ECB_PKCS1, encryptData, privateKey));
    }

    @Test
    public void signTest() {
        KeyPair keyPair = RSA.generateKeyPair(2048);
        Assert.assertNotNull(keyPair);

        RsaAlgorithms algorithms = RsaAlgorithms.RSA_MGF1_SHA256;
        final String data = "{春天里那个百花香，:我和妹妹把手牵:}";
        byte[] dateBytes = data.getBytes(StandardCharsets.UTF_8);
        byte[] signBytes = RSA.sign(algorithms, dateBytes, keyPair.getPrivate());
        Assert.assertTrue("验签结果：", RSA.verify(algorithms, dateBytes, keyPair.getPublic(), signBytes));
    }


}
