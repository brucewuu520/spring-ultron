package org.springultron.core.exception;

/**
 * 自定义加解密异常
 *
 * @author brucewuu
 * @date 2020/3/18 21:40
 */
public class CryptoException extends RuntimeException {
    private static final long serialVersionUID = 303660391539326286L;

    public CryptoException(String message) {
        super(message);
    }

    public CryptoException(Throwable cause) {
        super(Exceptions.getMessage(cause), cause);
    }

    public CryptoException(String message, Throwable cause) {
        super(message, cause);
    }
}
