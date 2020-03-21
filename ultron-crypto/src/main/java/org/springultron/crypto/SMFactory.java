package org.springultron.crypto;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.util.Arrays;
import org.springultron.core.exception.CryptoException;

import java.io.IOException;
import java.math.BigInteger;

/**
 * SM国密算法工具
 * <p>
 * 此工具类依赖org.bouncycastle:bcpkix-jdk15on
 * </p>
 *
 * @author brucewuu
 * @date 2020/3/19 23:17
 */
public enum SMFactory {
    INSTANCE;

    /**
     * SM2默认曲线
     */
    public static final String SM2_CURVE_NAME = "sm2p256v1";
    /**
     * SM2推荐曲线参数（来自https://github.com/ZZMarquis/gmhelper）
     */
    public final ECDomainParameters SM2_DOMAIN_PARAMS;

    public static final int RS_LEN = 32;

    SMFactory() {
        SM2_DOMAIN_PARAMS = BCUtils.toDomainParams(GMNamedCurves.getByName(SM2_CURVE_NAME));
    }

    /**
     * bc加解密使用旧标c1||c2||c3，此方法在加密后调用，将结果转化为c1||c3||c2
     *
     * @param c1c2c3             加密后的bytes，顺序为C1C2C3
     * @param ecDomainParameters {@link ECDomainParameters}
     * @return 加密后的bytes，顺序为C1C3C2
     */
    public static byte[] changeC1C2C3ToC1C3C2(byte[] c1c2c3, ECDomainParameters ecDomainParameters) {
        // sm2p256v1的这个固定65。可看GMNamedCurves、ECCurve代码。
        final int c1Len = (ecDomainParameters.getCurve().getFieldSize() + 7) / 8 * 2 + 1;
        final int c3Len = 32; // new SM3Digest().getDigestSize();
        byte[] result = new byte[c1c2c3.length];
        System.arraycopy(c1c2c3, 0, result, 0, c1Len); // c1
        System.arraycopy(c1c2c3, c1c2c3.length - c3Len, result, c1Len, c3Len); // c3
        System.arraycopy(c1c2c3, c1Len, result, c1Len + c3Len, c1c2c3.length - c1Len - c3Len); // c2
        return result;
    }

    /**
     * bc加解密使用旧标c1||c3||c2，此方法在解密前调用，将密文转化为c1||c2||c3再去解密
     *
     * @param c1c3c2             加密后的bytes，顺序为C1C3C2
     * @param ecDomainParameters {@link ECDomainParameters}
     * @return c1c2c3 加密后的bytes，顺序为C1C2C3
     */
    public static byte[] changeC1C3C2ToC1C2C3(byte[] c1c3c2, ECDomainParameters ecDomainParameters) {
        // sm2p256v1的这个固定65。可看GMNamedCurves、ECCurve代码。
        final int c1Len = (ecDomainParameters.getCurve().getFieldSize() + 7) / 8 * 2 + 1;
        final int c3Len = 32; // new SM3Digest().getDigestSize();
        byte[] result = new byte[c1c3c2.length];
        System.arraycopy(c1c3c2, 0, result, 0, c1Len); // c1: 0->65
        System.arraycopy(c1c3c2, c1Len + c3Len, result, c1Len, c1c3c2.length - c1Len - c3Len); // c2
        System.arraycopy(c1c3c2, c1Len, result, c1c3c2.length - c3Len, c3Len); // c3
        return result;
    }

    /**
     * BC的SM3withSM2签名得到的结果的rs是asn1格式的，这个方法转化成直接拼接r||s<br>
     * 来自：https://blog.csdn.net/pridas/article/details/86118774
     *
     * @param rsDer rs in asn1 format
     * @return sign result in plain byte array
     */
    public static byte[] rsAsn1ToPlain(byte[] rsDer) {
        ASN1Sequence seq = ASN1Sequence.getInstance(rsDer);
        byte[] r = bigIntToFixedLengthBytes(ASN1Integer.getInstance(seq.getObjectAt(0)).getValue());
        byte[] s = bigIntToFixedLengthBytes(ASN1Integer.getInstance(seq.getObjectAt(1)).getValue());
        byte[] result = new byte[RS_LEN * 2];
        System.arraycopy(r, 0, result, 0, r.length);
        System.arraycopy(s, 0, result, RS_LEN, s.length);

        return result;
    }

    /**
     * BC的SM3withSM2验签需要的rs是asn1格式的，这个方法将直接拼接r||s的字节数组转化成asn1格式<br>
     * 来自：https://blog.csdn.net/pridas/article/details/86118774
     *
     * @param sign in plain byte array
     * @return rs result in asn1 format
     */
    public static byte[] rsPlainToAsn1(byte[] sign) {
        if (sign.length != RS_LEN * 2) {
            throw new CryptoException("err rs. ");
        }
        BigInteger r = new BigInteger(1, Arrays.copyOfRange(sign, 0, RS_LEN));
        BigInteger s = new BigInteger(1, Arrays.copyOfRange(sign, RS_LEN, RS_LEN * 2));
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(new ASN1Integer(r));
        v.add(new ASN1Integer(s));
        try {
            return new DERSequence(v).getEncoded("DER");
        } catch (IOException e) {
            throw new CryptoException(e);
        }
    }

    /**
     * BigInteger转固定长度bytes
     *
     * @param rOrS {@link BigInteger}
     * @return 固定长度bytes
     */
    private static byte[] bigIntToFixedLengthBytes(BigInteger rOrS) {
        // for sm2p256v1, n is 00fffffffeffffffffffffffffffffffff7203df6b21c6052b53bbf40939d54123,
        // r and s are the result of mod n, so they should be less than n and have length<=32
        byte[] rs = rOrS.toByteArray();
        if (rs.length == RS_LEN) {
            return rs;
        } else if (rs.length == RS_LEN + 1 && rs[0] == 0) {
            return Arrays.copyOfRange(rs, 1, RS_LEN + 1);
        } else if (rs.length < RS_LEN) {
            byte[] result = new byte[RS_LEN];
            Arrays.fill(result, (byte) 0);
            System.arraycopy(rs, 0, result, RS_LEN - rs.length, rs.length);
            return result;
        } else {
            throw new CryptoException("Error rs: " + org.bouncycastle.util.encoders.Hex.toHexString(rs));
        }
    }
}