package org.springultron.crypto;

import java.security.Provider;

/**
 * 全局单例的 org.bouncycastle.jce.provider.BouncyCastleProvider 对象
 *
 * @author brucewuu
 * @date 2020/3/18 21:25
 */
public enum GlobalBouncyCastleProvider {
    INSTANCE;

    private Provider provider;
    private static boolean useBouncyCastle = true;

    GlobalBouncyCastleProvider() {
        try {
            this.provider = ProviderFactory.createBouncyCastleProvider();
        } catch (NoClassDefFoundError e) {
            // ignore
        }
    }

    /**
     * 设置是否使用Bouncy Castle库<br>
     * 如果设置为false，表示强制关闭Bouncy Castle而使用JDK
     *
     * @param isUseBouncyCastle 是否使用BouncyCastle库
     */
    public static void setUseBouncyCastle(boolean isUseBouncyCastle) {
        useBouncyCastle = isUseBouncyCastle;
    }

    /**
     * 获取{@link Provider}
     *
     * @return {@link Provider}
     */
    public Provider getProvider() {
        return useBouncyCastle ? this.provider : null;
    }
}
