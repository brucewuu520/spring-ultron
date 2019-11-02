package org.springultron.core.crypto;

import org.springultron.core.utils.Base64Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA公钥/私钥/签名加密解密
 * 默认补位方式为RSA/ECB/PKCS1Padding
 * <p>
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 *
 * @author brucewuu
 * @date 2019/10/28 22:36
 */
public class RSA {

    private RSA() {
    }

    private static final String KEY_ALGORITHM = "RSA";

//    public static byte[] encrypt(byte[] data, String publicKey, RsaAlgorithms algorithms) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
//        Cipher cipher = Cipher.getInstance(algorithms.getValue());
//        cipher.init(Cipher.ENCRYPT_MODE, generatePublicKey(publicKey));
//
//    }

    public static byte[] encrypt(byte[] data, PublicKey publicKey, RsaAlgorithms algorithms) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(algorithms.getValue());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 生成键值对
     *
     * @param algorithms 算法
     * @return {@link KeyPair}
     */
    public static KeyPair generateKeyPair(RsaAlgorithms algorithms) throws NoSuchAlgorithmException {
        int keySizeInBits = 1024;
        switch (algorithms) {
            case RSA_SHA256:
                keySizeInBits = 2048;
                break;
            case RSA_SHA384:
                keySizeInBits = 3072;
                break;
            case RSA_SHA512:
                keySizeInBits = 4096;
                break;
        }
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(keySizeInBits);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 获取公钥字符串 base64
     *
     * @param keyPair {@link KeyPair}
     */
    public static String getPublicKey(KeyPair keyPair) {
        PublicKey publicKey = keyPair.getPublic();
        return Base64Utils.encodeToString(publicKey.getEncoded());
    }

    /**
     * 获取私钥字符串 base64
     *
     * @param keyPair {@link KeyPair}
     */
    public static String getPrivateKey(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        return Base64Utils.encodeToString(privateKey.getEncoded());
    }

    /**
     * 将RSA公钥字符串转为 {@link PublicKey}
     *
     * @param publicKey 公钥字符串 base64
     * @return {@link PublicKey}
     */
    public static PublicKey generatePublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Base64Utils.decodeFromString(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 将RSA私钥字符串转为 {@link PrivateKey}
     *
     * @param privateKey 私钥字符串 base64
     * @return {@link PrivateKey}
     */
    public static PrivateKey generatePrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Base64Utils.decodeFromString(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }
}
