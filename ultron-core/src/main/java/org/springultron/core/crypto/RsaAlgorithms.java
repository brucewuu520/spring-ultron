package org.springultron.core.crypto;

/**
 * RSA加密算法类型
 *
 * @author brucewuu
 * @date 2019/10/27 22:19
 */
public enum RsaAlgorithms {
    /**
     * RSA默认算法
     */
    RSA("RSA", "RSA默认算法"),
    /**
     * RSA加密算法，此算法补位方式为RSA/ECB/PKCS1Padding
     */
    RSA_ECB_PKCS1("RSA/ECB/PKCS1Padding", "RSA加密算法，此算法补位方式为RSA/ECB/PKCS1Padding"),
    /**
     * RSA加密算法，此算法补位方式为RSA/None/NoPadding
     */
    RSA_NONE("RSA/None/NoPadding", "RSA机密算法，此算法补位方式为RSA/None/NoPadding"),
    /**
     * 签名算法：RSA-SSA-PKCS-v1_5 using MD5
     */
    RSA_MD5("MD5withRSA", "签名算法：RSA-SSA-PKCS-v1_5 using MD5"),
    /**
     * 签名算法：RSA-SSA-PKCS-v1_5 using SHA-1
     */
    RSA_SHA1("SHA1WithRSA", "签名算法：RSA-SSA-PKCS-v1_5 using SHA-1"),
    /**
     * 签名算法：RSA-SSA-PKCS-v1_5 using SHA-256
     */
    RSA_SHA256("SHA256withRSA", "签名算法：RSA-SSA-PKCS-v1_5 using SHA-256"),
    /**
     * 签名算法：RSA-SSA-PKCS-v1_5 using SHA-384
     */
    RSA_SHA384("SHA384withRSA", "签名算法：RSA-SSA-PKCS-v1_5 using SHA-384"),
    /**
     * 签名算法：RSA-SSA-PKCS-v1_5 using SHA-512
     */
    RSA_SHA512("SHA512withRSA", "签名算法：RSA-SSA-PKCS-v1_5 using SHA-512");

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
