package org.springultron.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

/**
 * List工具
 *
 * @Auther: brucewuu
 * @Date: 2019-06-07 12:52
 * @Description:
 */
public class Lists extends org.springframework.util.CollectionUtils {

    private Lists() {
    }

    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<>();
    }

    public static <E> ArrayList<E> newArrayList(final int initialCapacity) {
        return new ArrayList<>(initialCapacity);
    }

    public static <E> ArrayList<E> newArrayList(Collection<? extends E> collection) {
        Objects.requireNonNull(collection, "collection can not be null");
        return new ArrayList<>(collection);
    }

    public static <E> LinkedList<E> newLinkedList() {
        return new LinkedList<>();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
}
