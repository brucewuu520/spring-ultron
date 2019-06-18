package org.springultron.core.convert;

import java.util.function.Function;

/**
 * @Auther: brucewuu
 * @Date: 2019-05-27 15:07
 * @Description:
 */
public abstract class Converter<A, B> implements Function<A, B> {
    protected abstract B doForward(A a);

    protected abstract A doBackward(B b);

    @Override
    public B apply(A a) {
        return doForward(a);
    }
}