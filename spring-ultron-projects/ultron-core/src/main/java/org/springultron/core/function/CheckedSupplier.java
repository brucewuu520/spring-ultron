package org.springultron.core.function;

/**
 * 受检验的 Supplier
 *
 * @author brucewuu
 * @date 2020/4/28 11:05
 */
public interface CheckedSupplier<T> {
    /**
     * Gets a result.
     *
     * @return a result
     * @throws Throwable CheckedException
     */
    T get() throws Throwable;
}
