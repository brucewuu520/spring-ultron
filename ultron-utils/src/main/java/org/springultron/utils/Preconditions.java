package org.springultron.utils;

import org.springframework.lang.Nullable;

/**
 * @Auther: brucewuu
 * @Date: 2019-06-07 19:58
 * @Description:
 */
public final class Preconditions {

    private Preconditions() {
    }

    public static void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkArgument(boolean expression, @Nullable Object errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(errorMessage));
        }
    }

    public static void checkArgument(boolean expression, @Nullable String errorMessageTemplate, @Nullable Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalArgumentException(format(errorMessageTemplate, errorMessageArgs));
        }
    }

    public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, char p1) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1));
        }
    }

    public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, int p1) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1));
        }
    }

    public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, long p1) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1));
        }
    }

    public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1));
        }
    }

    public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, char p1, char p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, char p1, int p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, char p1, long p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, char p1, @Nullable Object p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, int p1, char p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, int p1, int p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, int p1, long p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, int p1, @Nullable Object p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, long p1, char p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, long p1, int p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, long p1, long p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, long p1, @Nullable Object p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, char p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, int p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, long p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2, @Nullable Object p3) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2, p3));
        }
    }

    public static void checkArgument(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2, @Nullable Object p3, @Nullable Object p4) {
        if (!b) {
            throw new IllegalArgumentException(format(errorMessageTemplate, p1, p2, p3, p4));
        }
    }

    public static void checkState(boolean expression) {
        if (!expression) {
            throw new IllegalStateException();
        }
    }

    public static void checkState(boolean expression, @Nullable Object errorMessage) {
        if (!expression) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }

    public static void checkState(boolean expression, @Nullable String errorMessageTemplate, @Nullable Object... errorMessageArgs) {
        if (!expression) {
            throw new IllegalStateException(format(errorMessageTemplate, errorMessageArgs));
        }
    }

    public static void checkState(boolean b, @Nullable String errorMessageTemplate, char p1) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1));
        }
    }

    public static void checkState(boolean b, @Nullable String errorMessageTemplate, int p1) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1));
        }
    }

    public static void checkState(boolean b, @Nullable String errorMessageTemplate, long p1) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1));
        }
    }

    public static void checkState(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1));
        }
    }

    public static void checkState(boolean b, @Nullable String errorMessageTemplate, char p1, char p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkState(boolean b, @Nullable String errorMessageTemplate, char p1, int p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkState(boolean b, @Nullable String errorMessageTemplate, char p1, long p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkState(boolean b, @Nullable String errorMessageTemplate, char p1, @Nullable Object p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkState(boolean b, @Nullable String errorMessageTemplate, int p1, char p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkState(boolean b, @Nullable String errorMessageTemplate, int p1, int p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkState(boolean b, @Nullable String errorMessageTemplate, int p1, long p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkState(boolean b, @Nullable String errorMessageTemplate, int p1, @Nullable Object p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkState(boolean b, @Nullable String errorMessageTemplate, long p1, char p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkState(boolean b, @Nullable String errorMessageTemplate, long p1, int p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkState(boolean b, @Nullable String errorMessageTemplate, long p1, long p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkState(boolean b, @Nullable String errorMessageTemplate, long p1, @Nullable Object p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkState(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, char p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkState(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, int p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkState(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, long p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkState(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2));
        }
    }

    public static void checkState(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2, @Nullable Object p3) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2, p3));
        }
    }

    public static void checkState(boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2, @Nullable Object p3, @Nullable Object p4) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1, p2, p3, p4));
        }
    }

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        } else {
            return reference;
        }
    }

    public static <T> T checkNotNull(T reference, @Nullable Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        } else {
            return reference;
        }
    }

    public static <T> T checkNotNull(T reference, @Nullable String errorMessageTemplate, @Nullable Object... errorMessageArgs) {
        if (reference == null) {
            throw new NullPointerException(format(errorMessageTemplate, errorMessageArgs));
        } else {
            return reference;
        }
    }

    public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, char p1) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1));
        } else {
            return obj;
        }
    }

    public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, int p1) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1));
        } else {
            return obj;
        }
    }

    public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, long p1) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1));
        } else {
            return obj;
        }
    }

    public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, @Nullable Object p1) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1));
        } else {
            return obj;
        }
    }

    public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, char p1, char p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        } else {
            return obj;
        }
    }

    public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, char p1, int p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        } else {
            return obj;
        }
    }

    public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, char p1, long p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        } else {
            return obj;
        }
    }

    public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, char p1, @Nullable Object p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        } else {
            return obj;
        }
    }

    public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, int p1, char p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        } else {
            return obj;
        }
    }

    public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, int p1, int p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        } else {
            return obj;
        }
    }

    public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, int p1, long p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        } else {
            return obj;
        }
    }

    public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, int p1, @Nullable Object p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        } else {
            return obj;
        }
    }

    public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, long p1, char p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        } else {
            return obj;
        }
    }

    public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, long p1, int p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        } else {
            return obj;
        }
    }

    public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, long p1, long p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        } else {
            return obj;
        }
    }

    public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, long p1, @Nullable Object p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        } else {
            return obj;
        }
    }

    public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, char p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        } else {
            return obj;
        }
    }

    public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, int p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        } else {
            return obj;
        }
    }

    public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, long p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        } else {
            return obj;
        }
    }

    public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2));
        } else {
            return obj;
        }
    }

    public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2, @Nullable Object p3) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2, p3));
        } else {
            return obj;
        }
    }

    public static <T> T checkNotNull(T obj, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2, @Nullable Object p3, @Nullable Object p4) {
        if (obj == null) {
            throw new NullPointerException(format(errorMessageTemplate, p1, p2, p3, p4));
        } else {
            return obj;
        }
    }

    public static int checkElementIndex(int index, int size) {
        return checkElementIndex(index, size, "index");
    }

    public static int checkElementIndex(int index, int size, @Nullable String desc) {
        if (index >= 0 && index < size) {
            return index;
        } else {
            throw new IndexOutOfBoundsException(badElementIndex(index, size, desc));
        }
    }

    private static String badElementIndex(int index, int size, String desc) {
        if (index < 0) {
            return format("%s (%s) must not be negative", desc, index);
        } else if (size < 0) {
            throw new IllegalArgumentException("negative size: " + size);
        } else {
            return format("%s (%s) must be less than size (%s)", desc, index, size);
        }
    }

    public static int checkPositionIndex(int index, int size) {
        return checkPositionIndex(index, size, "index");
    }

    public static int checkPositionIndex(int index, int size, @Nullable String desc) {
        if (index >= 0 && index <= size) {
            return index;
        } else {
            throw new IndexOutOfBoundsException(badPositionIndex(index, size, desc));
        }
    }

    private static String badPositionIndex(int index, int size, String desc) {
        if (index < 0) {
            return format("%s (%s) must not be negative", desc, index);
        } else if (size < 0) {
            throw new IllegalArgumentException("negative size: " + size);
        } else {
            return format("%s (%s) must not be greater than size (%s)", desc, index, size);
        }
    }

    public static void checkPositionIndexes(int start, int end, int size) {
        if (start < 0 || end < start || end > size) {
            throw new IndexOutOfBoundsException(badPositionIndexes(start, end, size));
        }
    }

    private static String badPositionIndexes(int start, int end, int size) {
        if (start >= 0 && start <= size) {
            return end >= 0 && end <= size ? format("end index (%s) must not be less than start index (%s)", end, start) : badPositionIndex(end, size, "end index");
        } else {
            return badPositionIndex(start, size, "start index");
        }
    }

    static String format(String template, @Nullable Object... args) {
        template = String.valueOf(template);
        StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;

        int i;
        int placeholderStart;
        for(i = 0; i < args.length; templateStart = placeholderStart + 2) {
            placeholderStart = template.indexOf("%s", templateStart);
            if (placeholderStart == -1) {
                break;
            }

            builder.append(template, templateStart, placeholderStart);
            builder.append(args[i++]);
        }

        builder.append(template, templateStart, template.length());
        if (i < args.length) {
            builder.append(" [");
            builder.append(args[i++]);

            while(i < args.length) {
                builder.append(", ");
                builder.append(args[i++]);
            }

            builder.append(']');
        }

        return builder.toString();
    }

    static int checkNonnegative(int value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(name + " cannot be negative but was: " + value);
        } else {
            return value;
        }
    }
}
