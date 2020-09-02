package org.springultron.crypto;

import org.springframework.util.Base64Utils;
import org.springultron.core.exception.CryptoException;
import org.springultron.core.utils.IoUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAKey;

/**
 * RSA公钥/私钥/签名/加密/解密算法实现
 * 默认补位方式为RSA/ECB/PKCS1Padding
 *
 * <p>
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 * </p>
 *
 * <p>
 * 针对 RSA 的几种填充方式：
 * RSA/ECB/NoPadding 钥模长-1
 * RSA/ECB/PKCS1Padding 钥模长 -11
 * RSA/ECB/OAEPWithSHA1AndMGF1Padding 钥模长-41
 * </br>
 * 举个例子，默认情况：
 * 对RSA/None/NoPadding 长度限制是 127(加密)和128(解密)
 * 而RSA/ECB/PKCS1Padding 长度限制是 117(加密)和128(解密)
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
    public static KeyPair generateKeyPair(int keySizeInBits) {
        return SecureUtils.generateKeyPair(KEY_ALGORITHM, keySizeInBits);
    }

    /**
     * 生成RSA签名秘钥对
     *
     * @param algorithms 算法 {@link RsaAlgorithms}
     */
    public static KeyPair generateKeyPair(RsaAlgorithms algorithms) {
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
     * 数字签名（base64）
     *
     * @param algorithms 签名算法
     * @param data       签名文本
     * @param privateKey 私钥字符串 base64
     * @return base64
     */
    public static String sign(RsaAlgorithms algorithms, String data, String privateKey) {
        final PrivateKey key = SecureUtils.generatePrivateKey(KEY_ALGORITHM, Base64Utils.decodeFromString(privateKey));
        byte[] signBytes = sign(algorithms, data.getBytes(StandardCharsets.UTF_8), key);
        return Base64Utils.encodeToString(signBytes);
    }

    /**
     * 数字签名
     *
     * @param algorithms 签名算法
     * @param data       签名文本
     * @param privateKey 私钥 {@link PrivateKey}
     */
    public static byte[] sign(RsaAlgorithms algorithms, byte[] data, PrivateKey privateKey) {
        try {
            Signature signature = SecureUtils.createSignature(algorithms.getValue());
            signature.initSign(privateKey);
            signature.update(data);
            return signature.sign();
        } catch (SignatureException | InvalidKeyException e) {
            throw new CryptoException(e);
        }
    }

    /**
     * 校验签名
     *
     * @param algorithms 签名算法
     * @param data       签名前的原始数据
     * @param publicKey  公钥字符串 base64
     * @param sign       签名后数据 base64
     */
    public static boolean verify(RsaAlgorithms algorithms, String data, String publicKey, String sign) {
        final PublicKey key = SecureUtils.generatePublicKey(KEY_ALGORITHM, Base64Utils.decodeFromString(publicKey));
        return verify(algorithms, data.getBytes(StandardCharsets.UTF_8), key, Base64Utils.decodeFromString(sign));

    }

    /**
     * 校验签名
     *
     * @param algorithms 签名算法
     * @param data       签名前的原始数据
     * @param publicKey  公钥 {@link PublicKey}
     * @param sign       签名后的数据
     */
    public static boolean verify(RsaAlgorithms algorithms, byte[] data, PublicKey publicKey, byte[] sign) {
        try {
            Signature signature = SecureUtils.createSignature(algorithms.getValue());
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(sign);
        } catch (SignatureException | InvalidKeyException e) {
            throw new CryptoException(e);
        }
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
    public static byte[] encryptByPublicKey(byte[] data, PublicKey publicKey) {
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
    public static byte[] encryptByPublicKey(byte[] data, PublicKey publicKey, int encryptBlockSize) {
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
        byte[] encryptBytes = encryptByPublicKey(algorithms, data.getBytes(StandardCharsets.UTF_8), publicKey, encryptBlockSize);
        return Base64Utils.encodeToString(encryptBytes);
    }

    /**
     * 公钥加密（加密结果 base64）
     *
     * @param algorithms 算法 {@link RsaAlgorithms}
     * @param data       待加密文本
     * @param publicKey  公钥 {@link PublicKey}
     */
    public static String encryptByPublicKey(RsaAlgorithms algorithms, String data, PublicKey publicKey) {
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
    public static String encryptByPublicKey(RsaAlgorithms algorithms, String data, PublicKey publicKey, int encryptBlockSize) {
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
        return encryptByPublicKey(algorithms, data, publicKey, -1);
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
        final PublicKey key = SecureUtils.generatePublicKey(KEY_ALGORITHM, Base64Utils.decodeFromString(publicKey));
        return encryptByPublicKey(algorithms, data, key, encryptBlockSize);
    }

    /**
     * 公钥加密
     *
     * @param algorithms 算法 {@link RsaAlgorithms}
     * @param data       待加密数据字节数组
     * @param publicKey  公钥 {@link PublicKey}
     */
    public static byte[] encryptByPublicKey(RsaAlgorithms algorithms, byte[] data, PublicKey publicKey) {
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
    public static byte[] encryptByPublicKey(RsaAlgorithms algorithms, byte[] data, PublicKey publicKey, int encryptBlockSize) {
        try {
            final Cipher cipher = createCipher(algorithms.getValue());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            if (encryptBlockSize < 0) {
                if (GlobalBouncyCastleProvider.INSTANCE.getProvider() == null) {
                    // 在非使用BC库情况下，blockSize使用默认的算法，加密数据长度 <= 模长-11
                    encryptBlockSize = ((RSAKey) publicKey).getModulus().bitLength() / 8 - 11;
                } else {
                    // 在引入BC库情况下，自动获取块大小
                    final int blockSize = cipher.getBlockSize();
                    if (blockSize > 0) {
                        encryptBlockSize = blockSize;
                    }
                }
            }
            return doFinalWithBlock(cipher, data, encryptBlockSize);
        } catch (Exception e) {
            throw new CryptoException(e);
        }
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
    public static byte[] encryptByPrivateKey(byte[] data, PrivateKey privateKey) {
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
    public static byte[] encryptByPrivateKey(byte[] data, PrivateKey privateKey, int encryptBlockSize) {
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
    public static String encryptByPrivateKey(RsaAlgorithms algorithms, String data, PrivateKey privateKey) {
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
    public static String encryptByPrivateKey(RsaAlgorithms algorithms, String data, PrivateKey privateKey, int encryptBlockSize) {
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
        final PrivateKey key = SecureUtils.generatePrivateKey(KEY_ALGORITHM, Base64Utils.decodeFromString(privateKey));
        return encryptByPrivateKey(algorithms, data, key, encryptBlockSize);
    }

    /**
     * 私钥加密
     *
     * @param algorithms 算法 {@link RsaAlgorithms}
     * @param data       待加密数据字节数组
     * @param privateKey 私钥 {@link PrivateKey}
     */
    public static byte[] encryptByPrivateKey(RsaAlgorithms algorithms, byte[] data, PrivateKey privateKey) {
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
    public static byte[] encryptByPrivateKey(RsaAlgorithms algorithms, byte[] data, PrivateKey privateKey, int encryptBlockSize) {
        try {
            final Cipher cipher = createCipher(algorithms.getValue());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            if (encryptBlockSize < 0) {
                if (GlobalBouncyCastleProvider.INSTANCE.getProvider() == null) {
                    // 在非使用BC库情况下，blockSize使用默认的算法，加密数据长度 <= 模长-11
                    encryptBlockSize = ((RSAKey) privateKey).getModulus().bitLength() / 8 - 11;
                } else {
                    // 在引入BC库情况下，自动获取块大小
                    final int blockSize = cipher.getBlockSize();
                    if (blockSize > 0) {
                        encryptBlockSize = blockSize;
                    }
                }
            }
            return doFinalWithBlock(cipher, data, encryptBlockSize);
        } catch (Exception e) {
            throw new CryptoException(e);
        }
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
    public static byte[] decryptByByPublicKey(byte[] data, PublicKey publicKey) {
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
    public static byte[] decryptByByPublicKey(byte[] data, PublicKey publicKey, int decryptBlockSize) {
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
    public static String decryptByByPublicKey(RsaAlgorithms algorithms, String data, PublicKey publicKey) {
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
    public static String decryptByByPublicKey(RsaAlgorithms algorithms, String data, PublicKey publicKey, int decryptBlockSize) {
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
        final PublicKey key = SecureUtils.generatePublicKey(KEY_ALGORITHM, Base64Utils.decodeFromString(publicKey));
        return decryptByByPublicKey(algorithms, data, key, decryptBlockSize);
    }

    /**
     * 公钥解密
     *
     * @param algorithms 算法 {@link RsaAlgorithms}
     * @param data       待解密数据字节数组
     * @param publicKey  公钥 {@link PublicKey}
     */
    public static byte[] decryptByByPublicKey(RsaAlgorithms algorithms, byte[] data, PublicKey publicKey) {
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
    public static byte[] decryptByByPublicKey(RsaAlgorithms algorithms, byte[] data, PublicKey publicKey, int decryptBlockSize) {
        try {
            final Cipher cipher = createCipher(algorithms.getValue());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            if (decryptBlockSize < 0) {
                if (GlobalBouncyCastleProvider.INSTANCE.getProvider() == null) {
                    // 在非使用BC库情况下，blockSize使用默认的算法
                    decryptBlockSize = ((RSAKey) publicKey).getModulus().bitLength() / 8;
                } else {
                    // 在引入BC库情况下，自动获取块大小
                    final int blockSize = cipher.getBlockSize();
                    if (blockSize > 0) {
                        decryptBlockSize = blockSize;
                    }
                }
            }
            return doFinalWithBlock(cipher, data, decryptBlockSize);
        } catch (Exception e) {
            throw new CryptoException(e);
        }
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
    public static byte[] decryptByByPrivateKey(byte[] data, PrivateKey privateKey) {
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
    public static byte[] decryptByByPrivateKey(byte[] data, PrivateKey privateKey, int decryptBlockSize) {
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
    public static String decryptByByPrivateKey(RsaAlgorithms algorithms, String data, PrivateKey privateKey) {
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
    public static String decryptByByPrivateKey(RsaAlgorithms algorithms, String data, PrivateKey privateKey, int decryptBlockSize) {
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
        final PrivateKey key = SecureUtils.generatePrivateKey(KEY_ALGORITHM, Base64Utils.decodeFromString(privateKey));
        return decryptByByPrivateKey(algorithms, data, key, decryptBlockSize);
    }

    /**
     * 私钥解密
     *
     * @param algorithms 算法 {@link RsaAlgorithms}
     * @param data       待解密数据字节数组
     * @param privateKey 私钥 {@link PrivateKey}
     */
    public static byte[] decryptByByPrivateKey(RsaAlgorithms algorithms, byte[] data, PrivateKey privateKey) {
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
    public static byte[] decryptByByPrivateKey(RsaAlgorithms algorithms, byte[] data, PrivateKey privateKey, int decryptBlockSize) {
        try {
            final Cipher cipher = createCipher(algorithms.getValue());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            if (decryptBlockSize < 0) {
                if (GlobalBouncyCastleProvider.INSTANCE.getProvider() == null) {
                    // 在非使用BC库情况下，blockSize使用默认的算法
                    decryptBlockSize = ((RSAKey) privateKey).getModulus().bitLength() / 8;
                } else {
                    // 在引入BC库情况下，自动获取块大小
                    final int blockSize = cipher.getBlockSize();
                    if (blockSize > 0) {
                        decryptBlockSize = blockSize;
                    }
                }
            }
            return doFinalWithBlock(cipher, data, decryptBlockSize);
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }

    /**
     * 创建{@link Cipher}
     *
     * @param algorithm 算法
     * @return {@link Cipher}
     */
    private static Cipher createCipher(String algorithm) throws NoSuchPaddingException, NoSuchAlgorithmException {
        Provider provider = GlobalBouncyCastleProvider.INSTANCE.getProvider();
        Cipher cipher;
        try {
            cipher = (provider == null) ? Cipher.getInstance(algorithm) : Cipher.getInstance(algorithm, provider);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            // 在Linux下，未引入BC库可能会导致RSA/ECB/PKCS1Padding算法无法找到，此时使用默认算法
            System.err.println(e.getMessage() + "  \"使用默认算法：RSA");
            cipher = Cipher.getInstance(RsaAlgorithms.RSA.getValue());
        }
        return cipher;
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
        BufferedOutputStream bufferedOutputStream = null;
        ByteArrayOutputStream outputStream = null;
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
            IoUtils.closeQuietly(outputStream);
            IoUtils.closeQuietly(bufferedOutputStream);
        }
    }
}