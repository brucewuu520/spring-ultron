package org.springultron.crypto;

import java.security.Provider;

/**
 * Provider对象生产法工厂类
 * <pre>
 *  1. 调用{@link #createBouncyCastleProvider()} 用于新建一个org.bouncycastle.jce.provider.BouncyCastleProvider对象
 * </pre>
 *
 * @author brucewuu
 * @date 2020/3/18 21:30
 */
public class ProviderFactory {

    /**
     * 创建Bouncy Castle 提供者<br>
     * 如果用户未引入bouncycastle库，则此方法抛出{@link NoClassDefFoundError} 异常
     *
     * @return {@link Provider}
     */
    public static Provider createBouncyCastleProvider() throws NoClassDefFoundError {
        return new org.bouncycastle.jce.provider.BouncyCastleProvider();
    }

}