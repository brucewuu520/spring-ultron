package org.springultron.crypto;

/**
 * RSA加密算法类型
 *
 * @author brucewuu
 * @date 2019/10/27 22:19
 */
public enum RsaAlgorithms {

    RSA("RSA", "RSA默认算法"),
    RSA_ECB_PKCS1("RSA/ECB/PKCS1Padding", "RSA加密算法，此算法补位方式为RSA/ECB/PKCS1Padding"),
    RSA_ECB_NONE("RSA/ECB/NoPadding", "RSA加密算法，此算法补位方式为RSA/ECB/NoPadding（需依赖BC库）"),
    RSA_NONE("RSA/None/NoPadding", "RSA加密算法，此算法补位方式为RSA/None/NoPadding（需依赖BC库）"),
    RSA_MD5("MD5withRSA", "签名算法：RSA-SSA-PKCS-v1_5 using MD5"),
    RSA_SHA1("SHA1WithRSA", "签名算法：RSA-SSA-PKCS-v1_5 using SHA-1"),
    RSA_SHA256("SHA256withRSA", "签名算法：RSA-SSA-PKCS-v1_5 using SHA-256"),
    RSA_SHA384("SHA384withRSA", "签名算法：RSA-SSA-PKCS-v1_5 using SHA-384"),
    RSA_SHA512("SHA512withRSA", "签名算法：RSA-SSA-PKCS-v1_5 using SHA-512"),
    RSA_MGF1_SHA256("SHA256withRSAandMGF1", "签名算法：此算法具有更高的安全级别，采用“SHA256withRSAandMGF1”算法，则会使得每次采用同一个私钥签名同一段文字，所得的签名值都是不同的");

    private final String value;
    private final String description;

    RsaAlgorithms(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
