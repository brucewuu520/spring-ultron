package org.springultron.crypto;

import org.springframework.util.Assert;
import org.springultron.core.exception.CryptoException;
import org.springultron.core.pool.StringPool;
import org.springultron.core.utils.*;

import javax.crypto.*;
import java.security.*;
import java.security.spec.*;

/**
 * 安全相关工具类
 * <p>
 * 加密分为三种：
 * 1、对称加密（symmetric），例如：AES、DES等<br>
 * 2、非对称加密（asymmetric），例如：RSA、DSA等<br>
 * 3、摘要加密（digest），例如：MD5、SHA-1、SHA-256、HMAC等<br>
 * </p>
 *
 * @author brucewuu
 * @date 2020/3/19 22:51
 */
public class SecureUtils {

    private SecureUtils() {
    }

    /**
     * 默认密钥字节数
     *
     * <pre>
     * RSA/DSA
     * Default KeySize 1024
     * KeySize must be a multiple of 64, ranging from 512 to 1024 (inclusive).
     * </pre>
     */
    public static final int DEFAULT_KEY_SIZE = 1024;

    /**
     * 生成 {@link SecretKey}，仅用于对称加密和摘要算法密钥生成
     *
     * @param algorithm 算法，支持PBE算法
     * @return {@link SecretKey}
     */
    public static SecretKey generateKey(String algorithm) {
        return generateKey(algorithm, -1);
    }

    /**
     * 生成 {@link SecretKey}，仅用于对称加密和摘要算法密钥生成
     *
     * @param algorithm 算法，支持PBE算法
     * @param keySize   密钥长度
     * @return {@link SecretKey}
     */
    public static SecretKey generateKey(String algorithm, final int keySize) {
        algorithm = getMainAlgorithm(algorithm);
        final KeyGenerator keyGenerator = getKeyGenerator(algorithm);
        if (keySize > 0) {
            keyGenerator.init(keySize);
        } else if ("AES".equals(algorithm)) {
            // 对于AES的密钥，除非指定，否则强制使用128位
            keyGenerator.init(128);
        }
        return keyGenerator.generateKey();
    }

    /**
     * 生成用于非对称加密的公钥和私钥，仅用于非对称加密<br>
     * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
     *
     * @param algorithm 非对称加密算法
     * @return {@link KeyPair}
     */
    public static KeyPair generateKeyPair(String algorithm) {
        return generateKeyPair(algorithm, DEFAULT_KEY_SIZE);
    }

    /**
     * 生成用于非对称加密的公钥和私钥<br>
     * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
     *
     * @param algorithm 非对称加密算法
     * @param keySize   密钥模（modulus ）长度
     * @return {@link KeyPair}
     */
    public static KeyPair generateKeyPair(String algorithm, int keySize) {
        return generateKeyPair(algorithm, keySize, null);
    }

    /**
     * 生成用于非对称加密的公钥和私钥<br>
     * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
     *
     * @param algorithm 非对称加密算法
     * @param keySize   密钥模（modulus ）长度
     * @param seed      种子
     * @return {@link KeyPair}
     */
    public static KeyPair generateKeyPair(String algorithm, int keySize, byte[] seed) {
        // SM2算法需要单独定义其曲线生成
        if ("SM2".equalsIgnoreCase(algorithm)) {
            final ECGenParameterSpec sm2p256v1 = new ECGenParameterSpec(SMFactory.SM2_CURVE_NAME);
            return generateKeyPair(algorithm, keySize, seed, sm2p256v1);
        }
        return generateKeyPair(algorithm, keySize, seed, (AlgorithmParameterSpec[]) null);
    }

    /**
     * 生成用于非对称加密的公钥和私钥<br>
     * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
     *
     * @param algorithm 非对称加密算法
     * @param params    {@link AlgorithmParameterSpec}
     * @return {@link KeyPair}
     */
    public static KeyPair generateKeyPair(String algorithm, AlgorithmParameterSpec params) {
        return generateKeyPair(algorithm, null, params);
    }

    /**
     * 生成用于非对称加密的公钥和私钥<br>
     * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
     *
     * @param algorithm 非对称加密算法
     * @param param     {@link AlgorithmParameterSpec}
     * @param seed      种子
     * @return {@link KeyPair}
     */
    public static KeyPair generateKeyPair(String algorithm, byte[] seed, AlgorithmParameterSpec param) {
        return generateKeyPair(algorithm, DEFAULT_KEY_SIZE, seed, param);
    }

    /**
     * 生成用于非对称加密的公钥和私钥<br>
     * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
     *
     * <p>
     * 对于非对称加密算法，密钥长度有严格限制，具体如下：
     *
     * <p>
     * <b>RSA：</b>
     * <pre>
     * RS256、PS256：2048 bits
     * RS384、PS384：3072 bits
     * RS512、RS512：4096 bits
     * </pre>
     *
     * <p>
     * <b>EC（Elliptic Curve）：</b>
     * <pre>
     * EC256：256 bits
     * EC384：384 bits
     * EC512：512 bits
     * </pre>
     *
     * @param algorithm 非对称加密算法
     * @param keySize   密钥模（modulus ）长度（单位bit）
     * @param seed      种子
     * @param params    {@link AlgorithmParameterSpec}
     * @return {@link KeyPair}
     */
    public static KeyPair generateKeyPair(String algorithm, int keySize, byte[] seed, AlgorithmParameterSpec... params) {
        return generateKeyPair(algorithm, keySize, RandomUtils.createSecureRandom(seed), params);
    }

    /**
     * 生成用于非对称加密的公钥和私钥<br>
     * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
     *
     * <p>
     * 对于非对称加密算法，密钥长度有严格限制，具体如下：
     *
     * <p>
     * <b>RSA：</b>
     * <pre>
     * RS256、PS256：2048 bits
     * RS384、PS384：3072 bits
     * RS512、RS512：4096 bits
     * </pre>
     *
     * <p>
     * <b>EC（Elliptic Curve）：</b>
     * <pre>
     * EC256：256 bits
     * EC384：384 bits
     * EC512：512 bits
     * </pre>
     *
     * @param algorithm 非对称加密算法
     * @param keySize   密钥模（modulus ）长度（单位bit）
     * @param random    {@link SecureRandom} 对象，创建时可选传入seed
     * @param params    {@link AlgorithmParameterSpec}
     * @return {@link KeyPair}
     */
    public static KeyPair generateKeyPair(String algorithm, int keySize, SecureRandom random, AlgorithmParameterSpec... params) {
        algorithm = getAlgorithmAfterWith(algorithm);
        final KeyPairGenerator keyPairGen = getKeyPairGenerator(algorithm);

        // 密钥模（modulus ）长度初始化定义
        if (keySize > 0) {
            // key长度适配修正
            if ("EC".equalsIgnoreCase(algorithm) && keySize > 256) {
                // 对于EC（EllipticCurve）算法，密钥长度有限制，在此使用默认256
                keySize = 256;
            }
            if (null != random) {
                keyPairGen.initialize(keySize, random);
            } else {
                keyPairGen.initialize(keySize);
            }
        }

        // 自定义初始化参数
        if (ArrayUtils.isNotEmpty(params)) {
            for (AlgorithmParameterSpec param : params) {
                if (null == param) {
                    continue;
                }
                try {
                    if (null != random) {
                        keyPairGen.initialize(param, random);
                    } else {
                        keyPairGen.initialize(param);
                    }
                } catch (InvalidAlgorithmParameterException e) {
                    throw new CryptoException(e);
                }
            }
        }
        return keyPairGen.generateKeyPair();
    }

    /**
     * 生成公钥，仅用于非对称加密<br>
     * 采用X509证书规范<br>
     * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
     *
     * @param algorithm 算法
     * @param key       密钥，必须为DER编码存储
     * @return 公钥 {@link PublicKey}
     */
    public static PublicKey generatePublicKey(String algorithm, byte[] key) {
        if (null == key) {
            return null;
        }
        return generatePublicKey(algorithm, new X509EncodedKeySpec(key));
    }

    /**
     * 生成公钥，仅用于非对称加密<br>
     * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
     *
     * @param algorithm 算法
     * @param keySpec   {@link KeySpec}
     * @return 公钥 {@link PublicKey}
     */
    public static PublicKey generatePublicKey(String algorithm, KeySpec keySpec) {
        if (null == keySpec) {
            return null;
        }
        algorithm = getAlgorithmAfterWith(algorithm);
        try {
            return getKeyFactory(algorithm).generatePublic(keySpec);
        } catch (final InvalidKeySpecException e) {
            throw new CryptoException(e);
        }
    }

    /**
     * 生成私钥，仅用于非对称加密<br>
     * 采用PKCS#8规范，此规范定义了私钥信息语法和加密私钥语法<br>
     * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
     *
     * @param algorithm 算法
     * @param key       密钥，必须为DER编码存储
     * @return 私钥 {@link PrivateKey}
     */
    public static PrivateKey generatePrivateKey(String algorithm, byte[] key) {
        if (null == key) {
            return null;
        }
        return generatePrivateKey(algorithm, new PKCS8EncodedKeySpec(key));
    }

    /**
     * 生成私钥，仅用于非对称加密<br>
     * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
     *
     * @param algorithm 算法
     * @param keySpec   {@link KeySpec}
     * @return 私钥 {@link PrivateKey}
     */
    public static PrivateKey generatePrivateKey(String algorithm, KeySpec keySpec) {
        if (null == keySpec) {
            return null;
        }
        algorithm = getAlgorithmAfterWith(algorithm);
        try {
            return getKeyFactory(algorithm).generatePrivate(keySpec);
        } catch (final InvalidKeySpecException e) {
            throw new CryptoException(e);
        }
    }

    /**
     * 获取{@link KeyGenerator}
     *
     * @param algorithm 对称加密算法
     * @return {@link KeyGenerator}
     */
    public static KeyGenerator getKeyGenerator(String algorithm) {
        final Provider provider = GlobalBouncyCastleProvider.INSTANCE.getProvider();
        KeyGenerator keyGenerator;
        try {
            keyGenerator = (null == provider) ? KeyGenerator.getInstance(getMainAlgorithm(algorithm)) : KeyGenerator.getInstance(getMainAlgorithm(algorithm), provider);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
        return keyGenerator;
    }

    /**
     * 获取{@link KeyPairGenerator}
     *
     * @param algorithm 非对称加密算法
     * @return {@link KeyPairGenerator}
     */
    public static KeyPairGenerator getKeyPairGenerator(String algorithm) {
        final Provider provider = GlobalBouncyCastleProvider.INSTANCE.getProvider();
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = (null == provider) ? KeyPairGenerator.getInstance(getMainAlgorithm(algorithm)) : KeyPairGenerator.getInstance(getMainAlgorithm(algorithm), provider);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
        return keyPairGenerator;
    }

    /**
     * 获取{@link KeyFactory}
     *
     * @param algorithm 非对称加密算法
     * @return {@link KeyFactory}
     */
    public static KeyFactory getKeyFactory(String algorithm) {
        final Provider provider = GlobalBouncyCastleProvider.INSTANCE.getProvider();
        KeyFactory keyFactory;
        try {
            keyFactory = (null == provider) ? KeyFactory.getInstance(getMainAlgorithm(algorithm)) : KeyFactory.getInstance(getMainAlgorithm(algorithm), provider);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
        return keyFactory;
    }

    /**
     * 获取{@link SecretKeyFactory}
     *
     * @param algorithm 对称加密算法
     * @return {@link SecretKeyFactory}
     */
    public static SecretKeyFactory getSecretKeyFactory(String algorithm) {
        final Provider provider = GlobalBouncyCastleProvider.INSTANCE.getProvider();
        SecretKeyFactory secretKeyFactory;
        try {
            secretKeyFactory = (null == provider) ? SecretKeyFactory.getInstance(getMainAlgorithm(algorithm)) : SecretKeyFactory.getInstance(getMainAlgorithm(algorithm), provider);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
        return secretKeyFactory;
    }

    /**
     * 获取主体算法名，例如RSA/ECB/PKCS1Padding的主体算法是RSA
     *
     * @param algorithm XXXwithXXX算法
     * @return 主体算法名
     */
    public static String getMainAlgorithm(String algorithm) {
        final int slashIndex = algorithm.indexOf(StringPool.SLASH);
        if (slashIndex > 0) {
            return algorithm.substring(0, slashIndex);
        }
        return algorithm;
    }

    /**
     * 获取用于密钥生成的算法
     * <p>
     * 获取XXXwithXXX算法的后半部分算法，如果为ECDSA或SM2，返回算法为EC
     * </p>
     *
     * @param algorithm XXXwithXXX算法
     * @return 算法
     */
    public static String getAlgorithmAfterWith(String algorithm) {
        Assert.notNull(algorithm, "algorithm must be not null !");
        int indexOfWith = StringUtils.lastIndexOfIgnoreCase(algorithm, "with");
        if (indexOfWith > 0) {
            algorithm = StringUtils.subSuf(algorithm, indexOfWith + "with".length());
        }
        if ("ECDSA".equalsIgnoreCase(algorithm) || "SM2".equalsIgnoreCase(algorithm)) {
            algorithm = "EC";
        }
        return algorithm;
    }

    /**
     * 创建{@link Cipher}
     *
     * @param algorithm 算法
     * @return {@link Cipher}
     */
    public static Cipher createCipher(final String algorithm) {
        Provider provider = GlobalBouncyCastleProvider.INSTANCE.getProvider();
        Cipher cipher;
        try {
            cipher = (provider == null) ? Cipher.getInstance(algorithm) : Cipher.getInstance(algorithm, provider);
        } catch (final NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new CryptoException(e);
        }
        return cipher;
    }

    /**
     * 创建{@link Signature}
     *
     * @param algorithm 算法
     * @return {@link Signature}
     */
    public static Signature createSignature(final String algorithm) {
        Provider provider = GlobalBouncyCastleProvider.INSTANCE.getProvider();
        Signature signature;
        try {
            signature = (provider == null) ? Signature.getInstance(algorithm) : Signature.getInstance(algorithm, provider);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
        return signature;
    }

    /**
     * 创建{@link MessageDigest}
     *
     * @param algorithm 算法
     * @return {@link MessageDigest}
     */
    public static MessageDigest createMessageDigest(String algorithm) {
        final Provider provider = GlobalBouncyCastleProvider.INSTANCE.getProvider();
        MessageDigest messageDigest;
        try {
            messageDigest = (null == provider) ? MessageDigest.getInstance(algorithm) : MessageDigest.getInstance(algorithm, provider);
        } catch (final NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
        return messageDigest;
    }

    /**
     * 创建{@link Mac}
     *
     * @param algorithm 算法
     * @return {@link Mac}
     */
    public static Mac createMac(String algorithm) {
        final Provider provider = GlobalBouncyCastleProvider.INSTANCE.getProvider();
        Mac mac;
        try {
            mac = (null == provider) ? Mac.getInstance(algorithm) : Mac.getInstance(algorithm, provider);
        } catch (final NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
        return mac;
    }

    /**
     * 解码字符串，可支持的编码如下：
     *
     * <pre>
     * 1. Hex（16进制）编码
     * 1. Base64编码
     * </pre>
     *
     * @param str 被解码的字符串
     * @return byte array
     */
    public static byte[] decode(String str) {
        return Hex.isHex(str) ? Hex.decodeHex(str) : Base64Utils.decodeFromString(str);
    }

    /**
     * 打开Bouncy Castle库的使用，全局有效
     */
    public static void enableBouncyCastle() {
        GlobalBouncyCastleProvider.setUseBouncyCastle(true);
    }

    /**
     * 强制关闭Bouncy Castle库的使用，全局有效
     */
    public static void disableBouncyCastle() {
        GlobalBouncyCastleProvider.setUseBouncyCastle(false);
    }
}
