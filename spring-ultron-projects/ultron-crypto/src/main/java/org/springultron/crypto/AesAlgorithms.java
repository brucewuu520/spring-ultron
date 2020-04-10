package org.springultron.crypto;

/**
 * AES加密算法类型
 *
 * @author brucewuu
 * @date 2019/11/15 21:49
 */
public enum AesAlgorithms {
    /**
     * 采用PKCS5Padding补位算法
     */
    AES_ECB_PKCS5("AES/ECB/PKCS5Padding", "采用PKCS5Padding补位算法"),
    /**
     * 采用PKCS7算法，完全兼容微信所使用的AES加密方式，aes的key必须是256byte长（比如32个字符）
     */
    AES_CBC_PKCS7("AES/CBC/NoPadding", "采用PKCS7算法，完全兼容微信所使用的AES加密方式，aes的key必须是256byte长（比如32个字符）");

    private final String value;
    private final String description;

    AesAlgorithms(String value, String description) {
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
