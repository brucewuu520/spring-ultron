package org.springultron.core.crypto;

import org.springultron.core.exception.Exceptions;
import org.springframework.util.Base64Utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA公钥/私钥/签名/加密/解密算法实现
 * 默认补位方式为RSA/ECB/PKCS1Padding
 * <p>
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 * </p>
 *
 * @author brucewuu
 * @date 2019/10/28 22:36
 */
public final class RSA {

    private RSA() {
    }

    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 生成秘钥对
     *
     * @param keySizeInBits 模长度：1024 2048 4096
     * @return {@link KeyPair}
     */
    public static KeyPair generateKeyPair(int keySizeInBits) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(keySizeInBits);
        return keyPairGenerator.genKeyPair();
    }

    /**
     * 生成RSA签名秘钥对
     *
     * @param algorithms 算法 {@link RsaAlgorithms}
     */
    public static KeyPair generateKeyPair(RsaAlgorithms algorithms) throws NoSuchAlgorithmException {
        int keySizeInBits;
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
            default:
                keySizeInBits = 1024;
                break;
        }
        return generateKeyPair(keySizeInBits);
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

    /**
     * 数字签名
     *
     * @param algorithms 签名算法
     * @param data       签名文本
     * @param privateKey 私钥字符串 base64
     * @return base64
     */
    public static String sign(RsaAlgorithms algorithms, String data, String privateKey) {
        try {
            byte[] signBytes = sign(algorithms, data.getBytes(StandardCharsets.UTF_8), generatePrivateKey(privateKey));
            return Base64Utils.encodeToString(signBytes);
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 数字签名
     *
     * @param algorithms 签名算法
     * @param data       签名文本
     * @param privateKey 私钥 {@link PrivateKey}
     */
    public static byte[] sign(RsaAlgorithms algorithms, byte[] data, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(algorithms.getValue());
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    /**
     * 校验签名
     *
     * @param algorithms 签名算法
     * @param data       明文
     * @param publicKey  公钥字符串 base64
     * @param sign       签名字符串 base64
     */
    public static boolean verify(RsaAlgorithms algorithms, String data, String publicKey, String sign) {
        try {
            return verify(algorithms, data.getBytes(StandardCharsets.UTF_8), generatePublicKey(publicKey), Base64Utils.decodeFromString(sign));
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 校验签名
     *
     * @param algorithms 签名算法
     * @param data       明文数据
     * @param publicKey  公钥 {@link PublicKey}
     * @param sign       签名后数据
     */
    public static boolean verify(RsaAlgorithms algorithms, byte[] data, PublicKey publicKey, byte[] sign) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(algorithms.getValue());
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(sign);
    }

    /**
     * 公钥加密（默认加密算法 加密结果 base64）
     *
     * @param data      待加密数据字节数组
     * @param publicKey 公钥字符串 base64
     */
    public static String encryptByPublicKey(String data, String publicKey) {
        return encryptByPublicKey(RsaAlgorithms.RSA_ECB_PKCS1, data, publicKey);
    }

    /**
     * 公钥加密（默认加密算法 加密结果 base64）
     * <p>
     * 当 encryptBlockSize > 0 且 data的长度大于加密块大小 开启分段加密
     * </p>
     *
     * @param data             待加密数据字节数组
     * @param publicKey        公钥字符串 base64
     * @param encryptBlockSize 加密块大小（生成秘钥对时模长度：keySizeInBits/8 - 11）
     */
    public static String encryptByPublicKey(String data, String publicKey, int encryptBlockSize) {
        return encryptByPublicKey(RsaAlgorithms.RSA_ECB_PKCS1, data, publicKey, encryptBlockSize);
    }

    /**
     * 公钥加密（默认加密算法）
     *
     * @param data      待加密数据字节数组
     * @param publicKey 公钥 {@link PublicKey}
     */
    public static byte[] encryptByPublicKey(byte[] data, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        return encryptByPublicKey(RsaAlgorithms.RSA_ECB_PKCS1, data, publicKey);
    }

    /**
     * 公钥加密（默认加密算法）
     * <p>
     * 当 encryptBlockSize > 0 且 data的长度大于加密块大小 开启分段加密
     * </p>
     *
     * @param data             待加密数据字节数组
     * @param publicKey        公钥 {@link PublicKey}
     * @param encryptBlockSize 加密块大小（生成秘钥对时模长度：keySizeInBits/8 - 11）
     */
    public static byte[] encryptByPublicKey(byte[] data, PublicKey publicKey, int encryptBlockSize) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        return encryptByPublicKey(RsaAlgorithms.RSA_ECB_PKCS1, data, publicKey, encryptBlockSize);
    }

    /**
     * 公钥加密（加密结果 base64）
     *
     * @param algorithms 算法 {@link RsaAlgorithms}
     * @param data       待加密文本
     * @param publicKey  公钥字符串 base64
     */
    public static String encryptByPublicKey(RsaAlgorithms algorithms, String data, String publicKey) {
        return encryptByPublicKey(algorithms, data, publicKey, -1);
    }

    /**
     * 公钥加密（加密结果 base64）
     * <p>
     * 当 encryptBlockSize > 0 且 data的长度大于加密块大小 开启分段加密
     * </p>
     *
     * @param algorithms       算法 {@link RsaAlgorithms}
     * @param data             待加密文本
     * @param publicKey        公钥字符串 base64
     * @param encryptBlockSize 加密块大小（生成秘钥对时模长度：keySizeInBits/8 - 11）
     */
    public static String encryptByPublicKey(RsaAlgorithms algorithms, String data, String publicKey, int encryptBlockSize) {
        byte[] encryptBytes = encryptByPublicKey(algorithms, data.getBytes(StandardCharsets.UTF_8), publicKey, -1);
        return Base64Utils.encodeToString(encryptBytes);
    }

    /**
     * 公钥加密（加密结果 base64）
     *
     * @param algorithms 算法 {@link RsaAlgorithms}
     * @param data       待加密文本
     * @param publicKey  公钥 {@link PublicKey}
     */
    public static String encryptByPublicKey(RsaAlgorithms algorithms, String data, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        return encryptByPublicKey(algorithms, data, publicKey, -1);
    }

    /**
     * 公钥加密（加密结果 base64）
     * <p>
     * 当 encryptBlockSize > 0 且 data的长度大于加密块大小 开启分段加密
     * </p>
     *
     * @param algorithms       算法 {@link RsaAlgorithms}
     * @param data             待加密文本
     * @param publicKey        公钥 {@link PublicKey}
     * @param encryptBlockSize 加密块大小（生成秘钥对时模长度：keySizeInBits/8 - 11）
     */
    public static String encryptByPublicKey(RsaAlgorithms algorithms, String data, PublicKey publicKey, int encryptBlockSize) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        byte[] encryptBytes = encryptByPublicKey(algorithms, data.getBytes(StandardCharsets.UTF_8), publicKey, encryptBlockSize);
        return Base64Utils.encodeToString(encryptBytes);
    }

    /**
     * 公钥加密
     *
     * @param algorithms 算法 {@link RsaAlgorithms}
     * @param data       待加密数据字节数组
     * @param publicKey  公钥字符串 base64
     */
    public static byte[] encryptByPublicKey(RsaAlgorithms algorithms, byte[] data, String publicKey) {
        try {
            return encryptByPublicKey(algorithms, data, generatePublicKey(publicKey), -1);
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 公钥加密
     * <p>
     * 当 encryptBlockSize > 0 且 data的长度大于加密块大小 开启分段加密
     * </p>
     *
     * @param algorithms       算法 {@link RsaAlgorithms}
     * @param data             待加密数据字节数组
     * @param publicKey        公钥字符串 base64
     * @param encryptBlockSize 加密块大小（生成秘钥对时模长度：keySizeInBits/8 - 11）
     */
    public static byte[] encryptByPublicKey(RsaAlgorithms algorithms, byte[] data, String publicKey, int encryptBlockSize) {
        try {
            return encryptByPublicKey(algorithms, data, generatePublicKey(publicKey), encryptBlockSize);
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 公钥加密
     *
     * @param algorithms 算法 {@link RsaAlgorithms}
     * @param data       待加密数据字节数组
     * @param publicKey  公钥 {@link PublicKey}
     */
    public static byte[] encryptByPublicKey(RsaAlgorithms algorithms, byte[] data, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        return encryptByPublicKey(algorithms, data, publicKey, -1);
    }

    /**
     * 公钥加密
     * 当 encryptBlockSize > 0 且 data的长度大于加密块大小 开启分段加密
     *
     * @param algorithms       算法 {@link RsaAlgorithms}
     * @param data             待加密数据字节数组
     * @param publicKey        公钥 {@link PublicKey}
     * @param encryptBlockSize 加密块大小（生成秘钥对时模长度：keySizeInBits/8 - 11）
     */
    public static byte[] encryptByPublicKey(RsaAlgorithms algorithms, byte[] data, PublicKey publicKey, int encryptBlockSize) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        Cipher cipher = Cipher.getInstance(algorithms.getValue());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return doFinalWithBlock(cipher, data, encryptBlockSize);
    }

    /**
     * 私钥加密（默认加密算法 加密结果 base64）
     *
     * @param data       待加密文本
     * @param privateKey 私钥字符串 base64
     */
    public static String encryptByPrivateKey(String data, String privateKey) {
        return encryptByPrivateKey(RsaAlgorithms.RSA_ECB_PKCS1, data, privateKey);
    }

    /**
     * 私钥加密（默认加密算法 加密结果 base64）
     * <p>
     * 当 encryptBlockSize > 0 且 data的长度大于加密块大小 开启分段加密
     * </p>
     *
     * @param data             待加密数据字节数组
     * @param privateKey       私钥字符串 base64
     * @param encryptBlockSize 加密块大小（生成秘钥对时模长度：keySizeInBits/8 - 11）
     */
    public static String encryptByPrivateKey(String data, String privateKey, int encryptBlockSize) {
        return encryptByPrivateKey(RsaAlgorithms.RSA_ECB_PKCS1, data, privateKey, encryptBlockSize);
    }

    /**
     * 私钥加密（默认加密算法）
     *
     * @param data       待加密文本
     * @param privateKey 私钥字符串 base64
     */
    public static byte[] encryptByPrivateKey(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        return encryptByPrivateKey(RsaAlgorithms.RSA_ECB_PKCS1, data, privateKey);
    }

    /**
     * 私钥加密（默认加密算法）
     * <p>
     * 当 encryptBlockSize > 0 且 data的长度大于加密块大小 开启分段加密
     * </p>
     *
     * @param data             待加密数据字节数组
     * @param privateKey       私钥字符串 base64
     * @param encryptBlockSize 加密块大小（生成秘钥对时模长度：keySizeInBits/8 - 11）
     */
    public static byte[] encryptByPrivateKey(byte[] data, PrivateKey privateKey, int encryptBlockSize) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        return encryptByPrivateKey(RsaAlgorithms.RSA_ECB_PKCS1, data, privateKey, encryptBlockSize);
    }

    /**
     * 私钥加密（加密结果 base64）
     *
     * @param algorithms 算法 {@link RsaAlgorithms}
     * @param data       待加密文本
     * @param privateKey 私钥字符串 base64
     */
    public static String encryptByPrivateKey(RsaAlgorithms algorithms, String data, String privateKey) {
        return encryptByPrivateKey(algorithms, data, privateKey, -1);
    }

    /**
     * 私钥加密（加密结果 base64）
     * <p>
     * 当 encryptBlockSize > 0 且 data的长度大于加密块大小 开启分段加密
     * </p>
     *
     * @param algorithms       算法 {@link RsaAlgorithms}
     * @param data             待加密数据字节数组
     * @param privateKey       私钥字符串 base64
     * @param encryptBlockSize 加密块大小（生成秘钥对时模长度：keySizeInBits/8 - 11）
     */
    public static String encryptByPrivateKey(RsaAlgorithms algorithms, String data, String privateKey, int encryptBlockSize) {
        byte[] encryptBytes = encryptByPrivateKey(algorithms, data.getBytes(StandardCharsets.UTF_8), privateKey, encryptBlockSize);
        return Base64Utils.encodeToString(encryptBytes);
    }

    /**
     * 私钥加密（加密结果 base64）
     *
     * @param algorithms 算法 {@link RsaAlgorithms}
     * @param data       待加密文本
     * @param privateKey 私钥 {@link PrivateKey}
     */
    public static String encryptByPrivateKey(RsaAlgorithms algorithms, String data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        return encryptByPrivateKey(algorithms, data, privateKey, -1);
    }

    /**
     * 私钥加密（加密结果 base64）
     * <p>
     * 当 encryptBlockSize > 0 且 data的长度大于加密块大小 开启分段加密
     * </p>
     *
     * @param algorithms       算法 {@link RsaAlgorithms}
     * @param data             待加密数据字节数组
     * @param privateKey       私钥 {@link PrivateKey}
     * @param encryptBlockSize 加密块大小（生成秘钥对时模长度：keySizeInBits/8 - 11）
     */
    public static String encryptByPrivateKey(RsaAlgorithms algorithms, String data, PrivateKey privateKey, int encryptBlockSize) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        byte[] encryptBytes = encryptByPrivateKey(algorithms, data.getBytes(StandardCharsets.UTF_8), privateKey, encryptBlockSize);
        return Base64Utils.encodeToString(encryptBytes);
    }

    /**
     * 私钥加密
     *
     * @param algorithms 算法 {@link RsaAlgorithms}
     * @param data       待加密数据字节数组
     * @param privateKey 私钥字符串 base64
     */
    public static byte[] encryptByPrivateKey(RsaAlgorithms algorithms, byte[] data, String privateKey) {
        return encryptByPrivateKey(algorithms, data, privateKey, -1);
    }

    /**
     * 私钥加密
     * <p>
     * 当 encryptBlockSize > 0 且 data的长度大于加密块大小 开启分段加密
     * </p>
     *
     * @param algorithms       算法 {@link RsaAlgorithms}
     * @param data             待加密数据字节数组
     * @param privateKey       私钥字符串 base64
     * @param encryptBlockSize 加密块大小（生成秘钥对时模长度：keySizeInBits/8 - 11）
     */
    public static byte[] encryptByPrivateKey(RsaAlgorithms algorithms, byte[] data, String privateKey, int encryptBlockSize) {
        try {
            return encryptByPrivateKey(algorithms, data, generatePrivateKey(privateKey), encryptBlockSize);
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 私钥加密
     *
     * @param algorithms 算法 {@link RsaAlgorithms}
     * @param data       待加密数据字节数组
     * @param privateKey 私钥 {@link PrivateKey}
     */
    public static byte[] encryptByPrivateKey(RsaAlgorithms algorithms, byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        return encryptByPrivateKey(algorithms, data, privateKey, -1);
    }

    /**
     * 私钥加密
     * <p>
     * 当 encryptBlockSize > 0 且 data的长度大于加密块大小 开启分段加密
     * </p>
     *
     * @param algorithms       算法 {@link RsaAlgorithms}
     * @param data             待加密数据字节数组
     * @param privateKey       私钥 {@link PrivateKey}
     * @param encryptBlockSize 加密块大小（生成秘钥对时模长度：keySizeInBits/8 - 11）
     */
    public static byte[] encryptByPrivateKey(RsaAlgorithms algorithms, byte[] data, PrivateKey privateKey, int encryptBlockSize) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        Cipher cipher = Cipher.getInstance(algorithms.getValue());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return doFinalWithBlock(cipher, data, encryptBlockSize);
    }

    /**
     * 公钥解密（默认解密算法）
     *
     * @param data      待解密文本
     * @param publicKey 公钥字符串 base64
     */
    public static String decryptByByPublicKey(String data, String publicKey) {
        return decryptByByPublicKey(RsaAlgorithms.RSA_ECB_PKCS1, data, publicKey);
    }

    /**
     * 公钥解密（默认解密算法）
     * <p>
     * 当 decryptBlockSize > 0 且 data的长度大于解密块大小 开启分段解密
     * </p>
     *
     * @param data             待解密文本 base64
     * @param publicKey        公钥字符串 base64
     * @param decryptBlockSize 解密块大小（生成秘钥对时模长度：keySizeInBits/8）
     */
    public static String decryptByByPublicKey(String data, String publicKey, int decryptBlockSize) {
        return decryptByByPublicKey(RsaAlgorithms.RSA_ECB_PKCS1, data, publicKey, decryptBlockSize);
    }

    /**
     * 公钥解密（默认解密算法）
     *
     * @param data      待解密数据字节数组
     * @param publicKey 公钥 {@link PublicKey}
     */
    public static byte[] decryptByByPublicKey(byte[] data, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        return decryptByByPublicKey(RsaAlgorithms.RSA_ECB_PKCS1, data, publicKey);
    }

    /**
     * 公钥解密（默认解密算法）
     * <p>
     * 当 decryptBlockSize > 0 且 data的长度大于解密块大小 开启分段解密
     * </p>
     *
     * @param data             待解密数据字节数组
     * @param publicKey        公钥 {@link PublicKey}
     * @param decryptBlockSize 解密块大小（生成秘钥对时模长度：keySizeInBits/8）
     */
    public static byte[] decryptByByPublicKey(byte[] data, PublicKey publicKey, int decryptBlockSize) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        return decryptByByPublicKey(RsaAlgorithms.RSA_ECB_PKCS1, data, publicKey, decryptBlockSize);
    }

    /**
     * 公钥解密
     *
     * @param algorithms 算法 {@link RsaAlgorithms}
     * @param data       待解密文本
     * @param publicKey  公钥字符串 base64
     */
    public static String decryptByByPublicKey(RsaAlgorithms algorithms, String data, String publicKey) {
        return decryptByByPublicKey(algorithms, data, publicKey, -1);
    }

    /**
     * 公钥解密
     * <p>
     * 当 decryptBlockSize > 0 且 data的长度大于解密块大小 开启分段解密
     * </p>
     *
     * @param algorithms       算法 {@link RsaAlgorithms}
     * @param data             待解密文本 base64
     * @param publicKey        公钥字符串 base64
     * @param decryptBlockSize 解密块大小（生成秘钥对时模长度：keySizeInBits/8）
     */
    public static String decryptByByPublicKey(RsaAlgorithms algorithms, String data, String publicKey, int decryptBlockSize) {
        byte[] decryptBytes = decryptByByPublicKey(algorithms, Base64Utils.decodeFromString(data), publicKey, decryptBlockSize);
        return new String(decryptBytes, StandardCharsets.UTF_8);
    }

    /**
     * 公钥解密
     *
     * @param algorithms 算法 {@link RsaAlgorithms}
     * @param data       待解密文本
     * @param publicKey  公钥 {@link PublicKey}
     */
    public static String decryptByByPublicKey(RsaAlgorithms algorithms, String data, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        return decryptByByPublicKey(algorithms, data, publicKey, -1);
    }

    /**
     * 公钥解密
     * <p>
     * 当 decryptBlockSize > 0 且 data的长度大于解密块大小 开启分段解密
     * </p>
     *
     * @param algorithms       算法 {@link RsaAlgorithms}
     * @param data             待解密文本 base64
     * @param publicKey        公钥 {@link PublicKey}
     * @param decryptBlockSize 解密块大小（生成秘钥对时模长度：keySizeInBits/8）
     */
    public static String decryptByByPublicKey(RsaAlgorithms algorithms, String data, PublicKey publicKey, int decryptBlockSize) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        byte[] decryptBytes = decryptByByPublicKey(algorithms, Base64Utils.decodeFromString(data), publicKey, decryptBlockSize);
        return new String(decryptBytes, StandardCharsets.UTF_8);
    }

    /**
     * 公钥解密
     *
     * @param algorithms 算法 {@link RsaAlgorithms}
     * @param data       待解密数据字节数组
     * @param publicKey  公钥字符串 base64
     */
    public static byte[] decryptByByPublicKey(RsaAlgorithms algorithms, byte[] data, String publicKey) {
        return decryptByByPublicKey(algorithms, data, publicKey, -1);
    }

    /**
     * 公钥解密
     * <p>
     * 当 decryptBlockSize > 0 且 data的长度大于解密块大小 开启分段解密
     * </p>
     *
     * @param algorithms       算法 {@link RsaAlgorithms}
     * @param data             待解密数据字节数组
     * @param publicKey        公钥字符串 base64
     * @param decryptBlockSize 解密块大小（生成秘钥对时模长度：keySizeInBits/8）
     */
    public static byte[] decryptByByPublicKey(RsaAlgorithms algorithms, byte[] data, String publicKey, int decryptBlockSize) {
        try {
            return decryptByByPublicKey(algorithms, data, generatePublicKey(publicKey), decryptBlockSize);
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 公钥解密
     *
     * @param algorithms 算法 {@link RsaAlgorithms}
     * @param data       待解密数据字节数组
     * @param publicKey  公钥 {@link PublicKey}
     */
    public static byte[] decryptByByPublicKey(RsaAlgorithms algorithms, byte[] data, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        return decryptByByPublicKey(algorithms, data, publicKey, -1);
    }

    /**
     * 公钥解密
     * <p>
     * 当 decryptBlockSize > 0 且 data的长度大于解密块大小 开启分段解密
     * </p>
     *
     * @param algorithms       算法 {@link RsaAlgorithms}
     * @param data             待解密数据字节数组
     * @param publicKey        公钥 {@link PublicKey}
     * @param decryptBlockSize 解密块大小（生成秘钥对时模长度：keySizeInBits/8）
     */
    public static byte[] decryptByByPublicKey(RsaAlgorithms algorithms, byte[] data, PublicKey publicKey, int decryptBlockSize) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        Cipher cipher = Cipher.getInstance(algorithms.getValue());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return doFinalWithBlock(cipher, data, decryptBlockSize);
    }

    /**
     * 私钥解密（默认解密算法）
     *
     * @param data       待解密文本 base64
     * @param privateKey 私钥字符串 base64
     */
    public static String decryptByByPrivateKey(String data, String privateKey) {
        return decryptByByPrivateKey(RsaAlgorithms.RSA_ECB_PKCS1, data, privateKey);
    }

    /**
     * 私钥解密（默认解密算法）
     * <p>
     * 当 decryptBlockSize > 0 且 data的长度大于解密块大小 开启分段解密
     * </p>
     *
     * @param data             待解密文本 base64
     * @param privateKey       私钥字符串 base64
     * @param decryptBlockSize 解密块大小（生成秘钥对时模长度：keySizeInBits/8）
     */
    public static String decryptByByPrivateKey(String data, String privateKey, int decryptBlockSize) {
        return decryptByByPrivateKey(RsaAlgorithms.RSA_ECB_PKCS1, data, privateKey, decryptBlockSize);
    }

    /**
     * 私钥解密（默认解密算法）
     *
     * @param data       待解密数据字节数组
     * @param privateKey 私钥 {@link PrivateKey}
     */
    public static byte[] decryptByByPrivateKey(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        return decryptByByPrivateKey(RsaAlgorithms.RSA_ECB_PKCS1, data, privateKey);
    }

    /**
     * 私钥解密（默认解密算法）
     * <p>
     * 当 decryptBlockSize > 0 且 data的长度大于解密块大小 开启分段解密
     * </p>
     *
     * @param data             待解密文本 base64
     * @param privateKey       私钥字符串 base64
     * @param decryptBlockSize 解密块大小（生成秘钥对时模长度：keySizeInBits/8）
     */
    public static byte[] decryptByByPrivateKey(byte[] data, PrivateKey privateKey, int decryptBlockSize) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        return decryptByByPrivateKey(RsaAlgorithms.RSA_ECB_PKCS1, data, privateKey, decryptBlockSize);
    }

    /**
     * 私钥解密
     *
     * @param algorithms 算法 {@link RsaAlgorithms}
     * @param data       待解密文本 base64
     * @param privateKey 私钥字符串 base64
     */
    public static String decryptByByPrivateKey(RsaAlgorithms algorithms, String data, String privateKey) {
        return decryptByByPrivateKey(algorithms, data, privateKey, -1);
    }

    /**
     * 私钥解密
     * <p>
     * 当 decryptBlockSize > 0 且 data的长度大于解密块大小 开启分段解密
     * </p>
     *
     * @param algorithms       算法 {@link RsaAlgorithms}
     * @param data             待解密文本 base64
     * @param privateKey       私钥字符串 base64
     * @param decryptBlockSize 解密块大小（生成秘钥对时模长度：keySizeInBits/8）
     */
    public static String decryptByByPrivateKey(RsaAlgorithms algorithms, String data, String privateKey, int decryptBlockSize) {
        byte[] decryptBytes = decryptByByPrivateKey(algorithms, Base64Utils.decodeFromString(data), privateKey, decryptBlockSize);
        return new String(decryptBytes, StandardCharsets.UTF_8);
    }

    /**
     * 私钥解密
     *
     * @param algorithms 算法 {@link RsaAlgorithms}
     * @param data       待解密文本 base64
     * @param privateKey 私钥 {@link PrivateKey}
     */
    public static String decryptByByPrivateKey(RsaAlgorithms algorithms, String data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        return decryptByByPrivateKey(algorithms, data, privateKey, -1);
    }

    /**
     * 私钥解密
     * <p>
     * 当 decryptBlockSize > 0 且 data的长度大于解密块大小 开启分段解密
     * </p>
     *
     * @param algorithms       算法 {@link RsaAlgorithms}
     * @param data             待解密文本 base64
     * @param privateKey       私钥 {@link PrivateKey}
     * @param decryptBlockSize 解密块大小（生成秘钥对时模长度：keySizeInBits/8）
     */
    public static String decryptByByPrivateKey(RsaAlgorithms algorithms, String data, PrivateKey privateKey, int decryptBlockSize) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        byte[] decryptBytes = decryptByByPrivateKey(algorithms, Base64Utils.decodeFromString(data), privateKey, decryptBlockSize);
        return new String(decryptBytes, StandardCharsets.UTF_8);
    }

    /**
     * 私钥解密
     *
     * @param algorithms 算法 {@link RsaAlgorithms}
     * @param data       待解密数据字节数组
     * @param privateKey 私钥字符串 base64
     */
    public static byte[] decryptByByPrivateKey(RsaAlgorithms algorithms, byte[] data, String privateKey) {
        return decryptByByPrivateKey(algorithms, data, privateKey, -1);
    }

    /**
     * 私钥解密
     * <p>
     * 当 decryptBlockSize > 0 且 data的长度大于解密块大小 开启分段解密
     * </p>
     *
     * @param algorithms       算法 {@link RsaAlgorithms}
     * @param data             待解密数据字节数组
     * @param privateKey       私钥字符串 base64
     * @param decryptBlockSize 解密块大小（生成秘钥对时模长度：keySizeInBits/8）
     */
    public static byte[] decryptByByPrivateKey(RsaAlgorithms algorithms, byte[] data, String privateKey, int decryptBlockSize) {
        try {
            return decryptByByPrivateKey(algorithms, data, generatePrivateKey(privateKey), decryptBlockSize);
        } catch (Exception e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 私钥解密
     *
     * @param algorithms 算法 {@link RsaAlgorithms}
     * @param data       待解密数据字节数组
     * @param privateKey 私钥 {@link PrivateKey}
     */
    public static byte[] decryptByByPrivateKey(RsaAlgorithms algorithms, byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        return decryptByByPrivateKey(algorithms, data, privateKey, -1);
    }

    /**
     * 私钥解密
     * <p>
     * 当 decryptBlockSize > 0 且 data的长度大于解密块大小 开启分段解密
     * </p>
     *
     * @param algorithms       算法 {@link RsaAlgorithms}
     * @param data             待解密数据字节数组
     * @param privateKey       私钥 {@link PrivateKey}
     * @param decryptBlockSize 解密块大小（生成秘钥对时模长度：keySizeInBits/8）
     */
    public static byte[] decryptByByPrivateKey(RsaAlgorithms algorithms, byte[] data, PrivateKey privateKey, int decryptBlockSize) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        Cipher cipher = Cipher.getInstance(algorithms.getValue());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return doFinalWithBlock(cipher, data, decryptBlockSize);
    }

    /**
     * 分段加密或解密
     * <p>
     * 当 encryptBlockSize > 0 且 data的长度大于加/解密块大小 开启分段加/解密
     * </p>
     *
     * @param cipher    加解密密工具 {@link Cipher}
     * @param data      待加密/待解密数据字节数组
     * @param blockSize 加/解密块大小
     */
    private static byte[] doFinalWithBlock(Cipher cipher, byte[] data, final int blockSize) throws BadPaddingException, IllegalBlockSizeException, IOException {
        ByteArrayOutputStream outputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            final int dataLength = data.length;
            final int maxBlockSize = blockSize < 0 ? dataLength : blockSize;
            // 不足分段
            if (dataLength <= maxBlockSize) {
                return cipher.doFinal(data, 0, dataLength);
            }
            outputStream = new ByteArrayOutputStream();
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            // 剩余长度
            int remainLength = dataLength;
            int offset = 0;
            int length;
            while (remainLength > 0) {
                length = Math.min(remainLength, maxBlockSize);
                bufferedOutputStream.write(cipher.doFinal(data, offset, length));
                offset += length;
                remainLength = dataLength - offset;
            }
            bufferedOutputStream.flush();
            return outputStream.toByteArray();
        } finally {
            try {
                if (null != bufferedOutputStream) {
                    bufferedOutputStream.close();
                }
                if (null != outputStream) {
                    outputStream.close();
                }
            } catch (IOException ignored) {
            }
        }
    }

//    public static void main(String[] args) throws Exception {
//        String data = "春天里那个百花香，我和妹妹把手牵";
//        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
////        RsaAlgorithms algorithms = RsaAlgorithms.RSA_NONE;
//        KeyPair keyPair = generateKeyPair(1024);
//        PublicKey publicKey = keyPair.getPublic();
//        PrivateKey privateKey = keyPair.getPrivate();
////        byte[] signBytes = sign(algorithms, dataBytes, privateKey);
////        System.err.println("数字签名:" + Base64Utils.encodeToString(signBytes));
////        System.err.println("签名校验:" + verify(algorithms, dataBytes, publicKey, signBytes));
//        String encryptData = encryptByPrivateKey(data, Base64Utils.encodeToString(privateKey.getEncoded()));
//        System.err.println("--加密后:" + encryptData);
//        String decryptData = decryptByByPublicKey(encryptData, Base64Utils.encodeToString(publicKey.getEncoded()));
//        System.err.println("--解密后:" + decryptData);
//    }
}
