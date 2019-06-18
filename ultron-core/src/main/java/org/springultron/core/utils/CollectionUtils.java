package org.springultron.core.utils;

import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

/**
 * 集合工具
 *
 * @Auther: brucewuu
 * @Date: 2019-06-07 12:52
 * @Description:
 */
public class CollectionUtils extends org.springframework.util.CollectionUtils {

    private CollectionUtils() {
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

    public static boolean isNotEmpty(@Nullable Collection<?> collection) {
        return !isEmpty(collection);
    }
}
