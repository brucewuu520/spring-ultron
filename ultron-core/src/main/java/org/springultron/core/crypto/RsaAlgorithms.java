package org.springultron.core.crypto;

/**
 * 加密算法类型
 *
 * @author brucewuu
 * @date 2019/10/27 22:19
 */
public enum RsaAlgorithms {
    RSA("RSA", "RSA算法"),
    RSA_ECB_PKCS1("RSA/ECB/PKCS1Padding", "RSA算法，此算法用了默认补位方式为RSA/ECB/PKCS1Padding"),
    RSA_NONE("RSA/None/NoPadding", "RSA算法，此算法用了RSA/None/NoPadding"),
    RSA_MD5("MD5withRSA", "RSA-SSA-PKCS-v1_5 using MD5"),
    RSA_SHA1("SHA1WithRSA", "RSA-SSA-PKCS-v1_5 using SHA-1"),
    RSA_SHA256("SHA256withRSA", "RSA-SSA-PKCS-v1_5 using SHA-256"),
    RSA_SHA384("SHA384withRSA", "RSA-SSA-PKCS-v1_5 using SHA-384"),
    RSA_SHA512("SHA512withRSA", "RSA-SSA-PKCS-v1_5 using SHA-512");

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
