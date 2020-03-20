package org.springultron.crypto;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.crypto.params.ECDomainParameters;

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

    private final static int RS_LEN = 32;

    SMFactory() {
        SM2_DOMAIN_PARAMS = BCUtils.toDomainParams(GMNamedCurves.getByName(SM2_CURVE_NAME));
    }

}