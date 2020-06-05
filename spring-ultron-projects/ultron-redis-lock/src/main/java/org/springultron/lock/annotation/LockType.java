package org.springultron.lock.annotation;

/**
 * 锁类型
 *
 * @author brucewuu
 * @date 2020/4/27 21:36
 */
public enum LockType {
    /**
     * 公平锁
     */
    FAIR,
    /**
     * 重入锁
     */
    REENTRANT
}
