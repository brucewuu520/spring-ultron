package org.springultron.core.convert;

import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

/**
 * Converter
 *
 * @author brucewuu
 * @date 2019-05-27 15:07
 */
public abstract class Converter<A, B> implements Function<A, B> {
    @Lazy
    private transient Converter<B, A> reverse;

    protected abstract B doForward(A a);

    protected abstract A doBackward(B b);

    public final B convert(A a) {
        return this.correctedDoForward(a);
    }

    B correctedDoForward(A a) {
        return Objects.requireNonNull(this.doForward(Objects.requireNonNull(a)));
    }

    A correctedDoBackward(B b) {
        return Objects.requireNonNull(this.doBackward(Objects.requireNonNull(b)));
    }

    public Iterable<B> convertAll(final Iterable<? extends A> fromIterable) {
        Objects.requireNonNull(fromIterable, "fromIterable can not be null");
        return new Iterable<B>() {
            @NonNull
            @Override
            public Iterator<B> iterator() {
                return new Iterator<B>() {
                    private final Iterator<? extends A> fromIterator = fromIterable.iterator();

                    @Override
                    public boolean hasNext() {
                        return this.fromIterator.hasNext();
                    }

                    @Override
                    public B next() {
                        return Converter.this.convert(this.fromIterator.next());
                    }

                    @Override
                    public void remove() {
                        this.fromIterator.remove();
                    }
                };
            }
        };
    }

    public Converter<B, A> reverse() {
        Converter<B, A> result = this.reverse;
        return result == null ? (this.reverse = new Converter.ReverseConverter<>(this)) : result;
    }

    @Deprecated
    @Override
    public B apply(A a) {
        return this.convert(a);
    }

    private static final class ReverseConverter<A, B> extends Converter<B, A> implements Serializable {
        private static final long serialVersionUID = 0L;
        final Converter<A, B> original;

        ReverseConverter(Converter<A, B> original) {
            this.original = original;
        }

        @Override
        protected A doForward(B b) {
            throw new AssertionError();
        }

        @Override
        protected B doBackward(A a) {
            throw new AssertionError();
        }

        @Override
        A correctedDoForward(B b) {
            return this.original.correctedDoBackward(b);
        }

        @Override
        B correctedDoBackward(A a) {
            return this.original.correctedDoForward(a);
        }

        @Override
        public Converter<A, B> reverse() {
            return this.original;
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof Converter.ReverseConverter) {
                Converter.ReverseConverter<?, ?> that = (Converter.ReverseConverter) object;
                return this.original.equals(that.original);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return ~this.original.hashCode();
        }

        @Override
        public String toString() {
            return this.original + ".reverse()";
        }
    }
}