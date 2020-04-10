package org.springultron.core.io;

import org.springframework.lang.NonNull;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

/**
 * @author brucewuu
 * @date 2020/3/25 11:51
 */
public class FastStringWriter extends Writer {
    /**
     * char buffer
     */
    private char[] buffer;
    /**
     * The number of chars in the buffer.
     */
    private int count;

    public FastStringWriter() {
        this(64);
    }

    public FastStringWriter(int initSize) {
        if (initSize < 0) {
            throw new IllegalArgumentException("initSize cannot be negative but was: " + initSize);
        }
        this.buffer = new char[initSize];
    }

    @Override
    public void write(int c) throws IOException {
        int newCount = count + 1;
        if (newCount > buffer.length) {
            buffer = Arrays.copyOf(buffer, Math.max(buffer.length << 1, newCount));
        }
        buffer[count] = (char) c;
        count = newCount;
    }

    @Override
    public void write(@NonNull char[] c, int off, int len) throws IOException {
        if ((off < 0) || (off > c.length) || (len < 0) || ((off + len) > c.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        int newCount = count + len;
        if (newCount > buffer.length) {
            buffer = Arrays.copyOf(buffer, Math.max(buffer.length << 1, newCount));
        }
        System.arraycopy(c, off, buffer, count, len);
        count = newCount;
    }

    @Override
    public void write(@NonNull String str, int off, int len) throws IOException {
        int newCount = count + len;
        if (newCount > buffer.length) {
            buffer = Arrays.copyOf(buffer, Math.max(buffer.length << 1, newCount));
        }
        str.getChars(off, off + len, buffer, count);
        count = newCount;
    }

    public void writeTo(Writer out) throws IOException {
        out.write(buffer, 0, count);
    }

    @Override
    public FastStringWriter append(CharSequence csq) throws IOException {
        String s = (csq == null ? "null" : csq.toString());
        write(s, 0, s.length());
        return this;
    }

    @Override
    public FastStringWriter append(CharSequence csq, int start, int end) throws IOException {
        String s = (csq == null ? "null" : csq).subSequence(start, end).toString();
        write(s, 0, s.length());
        return this;
    }

    @Override
    public FastStringWriter append(char c) throws IOException {
        write(c);
        return this;
    }

    public void reset() {
        count = 0;
    }

    public char[] toCharArray() {
        return Arrays.copyOf(buffer, count);
    }

    public int size() {
        return count;
    }

    @Override
    public String toString() {
        return new String(buffer, 0, count);
    }

    @Override
    public void flush() throws IOException {

    }

    @Override
    public void close() throws IOException {

    }
}
