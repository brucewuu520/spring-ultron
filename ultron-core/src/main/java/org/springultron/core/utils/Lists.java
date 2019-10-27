package org.springultron.core.utils;

import java.util.*;

/**
 * List工具
 *
 * @author brucewuu
 * @date 2019-06-07 12:52
 */
public final class Lists extends org.springframework.util.CollectionUtils {

    private Lists() {}

    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<>();
    }

    public static <E> ArrayList<E> newArrayList(final int initialCapacity) {
        return new ArrayList<>(initialCapacity);
    }

    public static <E> ArrayList<E> newArrayList(E... elements) {
        Objects.requireNonNull(elements, "elements can not be null");
        int capacity = computeArrayListCapacity(elements.length);
        ArrayList<E> list = new ArrayList<>(capacity);
        Collections.addAll(list, elements);
        return list;
    }

    public static <E> ArrayList<E> newArrayList(Collection<? extends E> collection) {
        Objects.requireNonNull(collection, "collection can not be null");
        return new ArrayList<>(collection);
    }

    public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) {
        Objects.requireNonNull(elements, "elements can not be null");
        return elements instanceof Collection ? new ArrayList<>(cast(elements)) : newArrayList(elements.iterator());
    }

    public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements) {
        ArrayList<E> list = new ArrayList<>();
        Lists.addAll(list, elements);
        return list;
    }

    public static <E> LinkedList<E> newLinkedList() {
        return new LinkedList<>();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static int getSize(Collection collection) {
        if (null == collection) {
            return 0;
        }
        return collection.size();
    }

    private static int computeArrayListCapacity(int length) {
        long value = 5L + (long)length + (long)(length / 10);
        if (value > 2147483647L) {
            return 2147483647;
        } else {
            return value < -2147483648L ? -2147483648 : (int)value;
        }
    }

    private static <T> Collection<T> cast(Iterable<T> iterable) {
        return (Collection<T>) iterable;
    }

    public static <T> boolean addAll(Collection<T> addTo, Iterator<? extends T> iterator) {
        Objects.requireNonNull(addTo);
        Objects.requireNonNull(iterator);

        boolean wasModified;
        wasModified = false;
        while (iterator.hasNext()) {
            wasModified |= addTo.add(iterator.next());
        }
        return wasModified;
    }
}
