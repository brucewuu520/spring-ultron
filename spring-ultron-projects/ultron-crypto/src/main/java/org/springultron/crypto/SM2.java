package org.springultron.crypto;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithID;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.signers.SM2Signer;
import org.springframework.lang.Nullable;
import org.springultron.core.exception.CryptoException;
import org.springultron.core.utils.Base64Utils;
import org.springultron.core.utils.Hex;
import org.springultron.core.utils.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 国密SM2算法实现，基于BC库
 *
 * <p>
 * 非对称加密，基于ECC，签名速度与秘钥生成速度都快于RSA。ECC 256位（SM2采用的就是ECC 256位的一种）安全强度比RSA 2048位高，但运算速度快于RSA。
 * </p>
 *
 * <p>
 * SM2非对称加密的结果由C1,C2,C3三部分组成:
 * 其中C1是生成随机数的计算出的椭圆曲线点，C2是密文数据，C3是SM3的摘要值；
 * 旧国密标准的结果是按C1C2C3顺序，新标准的是按C1C3C2顺序存放。
 * SM2算法只支持公钥加密，私钥解密 参考：https://blog.csdn.net/pridas/article/details/86118774
 * </p>
 *
 * @author brucewuu
 * @date 2020/3/19 21:18
 */
public class SM2 {

    private SM2() {
    }

    /**
     * 算法EC
     */
    private static final String ALGORITHM_NAME = "SM2";

    /**
     * 生成秘钥对
     *
     * @param keySizeInBits 模长度：1024 2048 4096
     * @return {@link KeyPair}
     */
    public static KeyPair generateKeyPair(int keySizeInBits) {
        return SecureUtils.generateKeyPair(ALGORITHM_NAME, keySizeInBits);
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
     * 公钥加密（base64）
     *
     * <p>
     * SM2非对称加密的结果由C1,C2,C3三部分组成:
     * 其中C1是生成随机数的计算出的椭圆曲线点，C2是密文数据，C3是SM3的摘要值；
     * 旧国密标准的结果是按C1C2C3顺序，新标准的是按C1C3C2顺序存放。默认使用新的标准：C1C3C2
     * </p>
     *
     * @param data      被加密字符串
     * @param publicKey 公钥字符串 base64
     * @return 加密后的 base64字符串
     */
    public static String encrypt(String data, String publicKey) {
        return encrypt(data, publicKey, SM2Engine.Mode.C1C3C2);
    }

    /**
     * 公钥加密（base64）
     *
     * <p>
     * SM2非对称加密的结果由C1,C2,C3三部分组成:
     * 其中C1是生成随机数的计算出的椭圆曲线点，C2是密文数据，C3是SM3的摘要值；
     * 旧国密标准的结果是按C1C2C3顺序，新标准的是按C1C3C2顺序存放。
     * </p>
     *
     * @param data      被加密字符串
     * @param publicKey 公钥字符串 base64
     * @param mode      加密结果组成顺序
     * @return 加密后的 base64字符串
     */
    public static String encrypt(String data, String publicKey, SM2Engine.Mode mode) {
        final PublicKey key = SecureUtils.generatePublicKey(ALGORITHM_NAME, Base64Utils.decodeFromString(publicKey));
        final byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        byte[] encryptBytes = encrypt(dataBytes, key, mode);
        return Base64Utils.encodeToString(encryptBytes);
    }

    /**
     * 公钥加密
     *
     * <p>
     * SM2非对称加密的结果由C1,C2,C3三部分组成:
     * 其中C1是生成随机数的计算出的椭圆曲线点，C2是密文数据，C3是SM3的摘要值；
     * 旧国密标准的结果是按C1C2C3顺序，新标准的是按C1C3C2顺序存放。默认使用新的标准：C1C3C2
     * </p>
     *
     * @param data      被加密的bytes
     * @param publicKey 公钥
     * @return 加密后的bytes
     */
    public static byte[] encrypt(byte[] data, PublicKey publicKey) {
        return encrypt(data, publicKey, SM2Engine.Mode.C1C3C2);
    }

    /**
     * 公钥加密
     *
     * <p>
     * SM2非对称加密的结果由C1,C2,C3三部分组成:
     * 其中C1是生成随机数的计算出的椭圆曲线点，C2是密文数据，C3是SM3的摘要值；
     * 旧国密标准的结果是按C1C2C3顺序，新标准的是按C1C3C2顺序存放。
     * </p>
     *
     * @param data      被加密的bytes
     * @param publicKey 公钥
     * @param mode      加密结果组成顺序
     * @return 加密后的bytes
     */
    public static byte[] encrypt(byte[] data, PublicKey publicKey, SM2Engine.Mode mode) {
        ECPublicKeyParameters publicKeyParameters = BCUtils.toParams(publicKey);
        CipherParameters cipherParameters = new ParametersWithRandom(publicKeyParameters);
        return encrypt(data, cipherParameters, mode);
    }

    /**
     * 公钥加密
     *
     * <p>
     * SM2非对称加密的结果由C1,C2,C3三部分组成:
     * 其中C1是生成随机数的计算出的椭圆曲线点，C2是密文数据，C3是SM3的摘要值；
     * 旧国密标准的结果是按C1C2C3顺序，新标准的是按C1C3C2顺序存放。默认使用新的标准：C1C3C2
     * </p>
     *
     * @param data                被加密的bytes
     * @param publicKeyParameters 公钥参数
     * @return 加密后的bytes
     */
    public static byte[] encrypt(byte[] data, CipherParameters publicKeyParameters) {
        return encrypt(data, publicKeyParameters, SM2Engine.Mode.C1C3C2);
    }

    /**
     * 公钥加密
     *
     * <p>
     * SM2非对称加密的结果由C1,C2,C3三部分组成:
     * 其中C1是生成随机数的计算出的椭圆曲线点，C2是密文数据，C3是SM3的摘要值；
     * 旧国密标准的结果是按C1C2C3顺序，新标准的是按C1C3C2顺序存放。默认使用新的标准：C1C3C2
     * </p>
     *
     * @param data                被加密的bytes
     * @param publicKeyParameters 公钥参数
     * @param mode                加密结果组成顺序
     * @return 加密后的bytes
     */
    public static byte[] encrypt(byte[] data, CipherParameters publicKeyParameters, SM2Engine.Mode mode) {
        SM2Engine engine = new SM2Engine(mode);
        engine.init(true, publicKeyParameters);
        try {
            return engine.processBlock(data, 0, data.length);
        } catch (final InvalidCipherTextException e) {
            throw new CryptoException(e);
        }
    }

    /**
     * 私钥解密
     *
     * @param data       SM2密文（base64），实际包含三部分：ECC公钥、真正的密文、公钥和原文的SM3-HASH值
     * @param privateKey 私钥
     * @return 加密后的bytes
     */
    public static String decrypt(String data, String privateKey) {
        return decrypt(data, privateKey, SM2Engine.Mode.C1C3C2);
    }

    /**
     * 私钥解密
     *
     * @param data       SM2密文（base64），实际包含三部分：ECC公钥、真正的密文、公钥和原文的SM3-HASH值
     * @param privateKey 私钥
     * @param mode       SM2密文组成顺序
     * @return 加密后的bytes
     */
    public static String decrypt(String data, String privateKey, SM2Engine.Mode mode) {
        final PrivateKey key = SecureUtils.generatePrivateKey(ALGORITHM_NAME, Base64Utils.decodeFromString(privateKey));
        byte[] dataBytes = Base64Utils.decodeFromString(data);
        byte[] decryptBytes = decrypt(dataBytes, key, mode);
        return new String(decryptBytes, StandardCharsets.UTF_8);
    }

    /**
     * 私钥解密
     *
     * @param data       SM2密文，实际包含三部分：ECC公钥、真正的密文、公钥和原文的SM3-HASH值
     * @param privateKey 私钥
     * @return 加密后的bytes
     */
    public static byte[] decrypt(byte[] data, PrivateKey privateKey) {
        return decrypt(data, privateKey, SM2Engine.Mode.C1C3C2);
    }

    /**
     * 私钥解密
     *
     * @param data       SM2密文，实际包含三部分：ECC公钥、真正的密文、公钥和原文的SM3-HASH值
     * @param privateKey 私钥
     * @param mode       SM2密文组成顺序
     * @return 加密后的bytes
     */
    public static byte[] decrypt(byte[] data, PrivateKey privateKey, SM2Engine.Mode mode) {
        ECPrivateKeyParameters privateKeyParameters = BCUtils.toParams(privateKey);
        return decrypt(data, privateKeyParameters, mode);
    }

    /**
     * 私钥解密
     *
     * @param data                 SM2密文，实际包含三部分：ECC公钥、真正的密文、公钥和原文的SM3-HASH值
     * @param privateKeyParameters 私钥参数
     * @return 加密后的bytes
     */
    public static byte[] decrypt(byte[] data, CipherParameters privateKeyParameters) {
        return decrypt(data, privateKeyParameters, SM2Engine.Mode.C1C3C2);
    }

    /**
     * 私钥解密
     *
     * @param data                 SM2密文，实际包含三部分：ECC公钥、真正的密文、公钥和原文的SM3-HASH值
     * @param privateKeyParameters 私钥参数
     * @param mode                 SM2密文组成顺序
     * @return 加密后的bytes
     */
    public static byte[] decrypt(byte[] data, CipherParameters privateKeyParameters, SM2Engine.Mode mode) {
        SM2Engine engine = new SM2Engine(mode);
        engine.init(false, privateKeyParameters);
        try {
            return engine.processBlock(data, 0, data.length);
        } catch (final InvalidCipherTextException e) {
            throw new CryptoException(e);
        }
    }

    /**
     * 用私钥对信息生成数字签名（Hex 16进制编码）
     *
     * @param data       被签名的数据
     * @param privateKey 私钥字符串（base64）
     * @return 签名 Hex 16进制编码
     */
    public static String signHex(String data, String privateKey) {
        return signHex(data, privateKey, null);
    }

    /**
     * 用私钥对信息生成数字签名（Hex 16进制编码）
     *
     * @param data       被签名的数据
     * @param privateKey 私钥字符串（base64）
     * @param hexId      Hex 16进制编码；可以为null，若为null，则默认withId为字节数组: "1234567812345678".getBytes()
     * @return 签名 Hex 16进制编码
     */
    public static String signHex(String data, String privateKey, @Nullable String hexId) {
        final PrivateKey key = SecureUtils.generatePrivateKey(ALGORITHM_NAME, Base64Utils.decodeFromString(privateKey));
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        byte[] idBytes = StringUtils.isEmpty(hexId) ? null : Hex.decodeHex(hexId);
        byte[] signBytes = sign(dataBytes, key, idBytes);
        return Hex.encodeHexString(signBytes);
    }

    /**
     * 用私钥对信息生成数字签名（base64）
     *
     * @param data       被签名的数据
     * @param privateKey 私钥字符串（base64）
     * @return 签名 base64
     */
    public static String sign(String data, String privateKey) {
        return sign(data, privateKey, null);
    }

    /**
     * 用私钥对信息生成数字签名（base64）
     *
     * @param data       被签名的数据
     * @param privateKey 私钥字符串（base64）
     * @param id         可以为null，若为null，则默认withId为字节数组: "1234567812345678".getBytes()
     * @return 签名 base64
     */
    public static String sign(String data, String privateKey, @Nullable String id) {
        final PrivateKey key = SecureUtils.generatePrivateKey(ALGORITHM_NAME, Base64Utils.decodeFromString(privateKey));
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        byte[] idBytes = null;
        if (StringUtils.isNotEmpty(id)) {
            idBytes = Hex.isHex(id) ? Hex.decodeHex(id) : id.getBytes(StandardCharsets.UTF_8);
        }
        byte[] signBytes = sign(dataBytes, key, idBytes);
        return Base64Utils.encodeToString(signBytes);
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       被签名的数据
     * @param privateKey 私钥
     * @param id         可以为null，若为null，则默认withId为字节数组: "1234567812345678".getBytes()
     * @return 签名 bytes
     */
    public static byte[] sign(byte[] data, PrivateKey privateKey, @Nullable byte[] id) {
        ECPrivateKeyParameters privateKeyParameters = BCUtils.toParams(privateKey);
        CipherParameters cipherParameters = new ParametersWithRandom(privateKeyParameters);
        return sign(data, cipherParameters, id);
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data 被签名的数据
     * @param id   可以为null，若为null，则默认withId为字节数组: "1234567812345678".getBytes()
     * @return 签名 bytes
     */
    public static byte[] sign(byte[] data, CipherParameters privateKeyParameters, @Nullable byte[] id) {
        SM2Signer signer = new SM2Signer();
        try {
            if (id != null) {
                privateKeyParameters = new ParametersWithID(privateKeyParameters, id);
            }
            signer.init(true, privateKeyParameters);
            signer.update(data, 0, data.length);
            return signer.generateSignature();
        } catch (org.bouncycastle.crypto.CryptoException e) {
            throw new CryptoException(e);
        }
    }

    /**
     * 用公钥检验数字签名的合法性
     *
     * @param data      签名前的原始数据
     * @param publicKey 公钥字符串（base64）
     * @param sign      签名后的数据
     * @return 是否验证通过
     */
    public static boolean verify(String data, String publicKey, String sign) {
        return verify(data, publicKey, sign, null);
    }

    /**
     * 用公钥检验数字签名的合法性
     *
     * @param data      签名前的原始数据
     * @param publicKey 公钥字符串（base64）
     * @param sign      签名后的数据
     * @param id        可以为null，若为null，则默认withId为字节数组: "1234567812345678".getBytes()
     * @return 是否验证通过
     */
    public static boolean verify(String data, String publicKey, String sign, @Nullable String id) {
        final PublicKey key = SecureUtils.generatePublicKey(ALGORITHM_NAME, Base64Utils.decodeFromString(publicKey));
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        byte[] signBytes = SecureUtils.decode(sign);
        byte[] idBytes = null;
        if (StringUtils.isNotEmpty(id)) {
            idBytes = Hex.isHex(id) ? Hex.decodeHex(id) : id.getBytes(StandardCharsets.UTF_8);
        }
        return verify(dataBytes, key, signBytes, idBytes);
    }

    /**
     * 用公钥检验数字签名的合法性
     *
     * @param data      签名前的原始数据
     * @param publicKey 公钥
     * @param sign      签名后的数据
     * @param id        可以为null，若为null，则默认withId为字节数组: "1234567812345678".getBytes()
     * @return 是否验证通过
     */
    public static boolean verify(byte[] data, PublicKey publicKey, byte[] sign, @Nullable byte[] id) {
        ECPublicKeyParameters publicKeyParameters = BCUtils.toParams(publicKey);
        return verify(data, publicKeyParameters, sign, id);
    }

    /**
     * 用公钥检验数字签名的合法性
     *
     * @param data 签名前的原始数据
     * @param sign 签名
     * @param id   可以为null，若为null，则默认withId为字节数组:"1234567812345678".getBytes()
     * @return 是否验证通过
     */
    public static boolean verify(byte[] data, CipherParameters publicKeyParameters, byte[] sign, @Nullable byte[] id) {
        final SM2Signer signer = new SM2Signer();
        if (id != null) {
            publicKeyParameters = new ParametersWithID(publicKeyParameters, id);
        }
        signer.init(false, publicKeyParameters);
        signer.update(data, 0, data.length);
        return signer.verifySignature(sign);
    }
}
