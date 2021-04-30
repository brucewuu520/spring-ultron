package org.springultron.core.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Map Utils
 *
 * @author brucewuu
 * @date 2019-06-07 12:54
 */
public class Maps {

    private Maps() {
    }

    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<>();
    }

    /**
     * init expectedSize HashMap
     *
     * @param expectedSize init size
     * @param <K>          key
     * @param <V>          value
     * @return HashMap
     */
    public static <K, V> HashMap<K, V> newHashMap(final int expectedSize) {
        return new HashMap<>(capacity(expectedSize));
    }

    private static int capacity(final int expectedSize) {
        if (expectedSize < 3) {
            if (expectedSize < 0) {
                throw new IllegalArgumentException("expectedSize cannot be negative but was: " + expectedSize);
            }
            return expectedSize + 1;
        } else {
            return expectedSize < 1073741824 ? (int) ((float) expectedSize / 0.75F + 1.0F) : 2147483647;
        }
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * map转签名字符串
     * 按照key的ASCII码排序；key=value&key=value&key=value
     *
     * @param map 集合
     * @return StringBuilder
     */
    public static StringBuilder sortSignValue(Map<String, ?> map) {
        List<String> keyList = new ArrayList<>(map.keySet());
        Collections.sort(keyList);
        StringBuilder builder = new StringBuilder();
        for (String key : keyList) {
            Object value = map.get(key);
            if (value != null) {
                if (builder.length() > 0) {
                    builder.append("&").append(key).append("=").append(value);
                } else {
                    builder.append(key).append("=").append(value);
                }
            }
        }
        return builder;
    }

    /**
     * queryString 转map
     * key=value&key=value&key=value专为键值对形式
     *
     * @param queryString key=value&key=value&key=value字符串
     * @return Map
     */
    @SuppressWarnings("ConstantConditions")
    public static Map<String, Object> queryStringToMap(String queryString) {
        if (StringUtils.isEmpty(queryString)) {
            return Collections.emptyMap();
        }
        String[] params = StringUtils.split(queryString, "&");
        if (ArrayUtils.isEmpty(params)) {
            return Collections.emptyMap();
        }
        Map<String, Object> hashMap = new HashMap<>();
        for (String param : params) {
            String[] kvArray = StringUtils.split(param, "=");
            int length = ArrayUtils.getLength(kvArray);
            if (length > 0) {
                hashMap.put(kvArray[0], length > 1 ? kvArray[1] : null);
            }
        }
        return hashMap;
    }

    /**
     * Gets a String from a Map in a null-safe manner.
     * <p>
     * The String is obtained via <code>toString</code>.
     *
     * @param map the map to use
     * @param key the key to look up
     * @return the value in the Map as a String, <code>null</code> if null map input
     */
    public static String getString(final Map<?, ?> map, final Object key) {
        if (map != null) {
            Object answer = map.get(key);
            if (answer != null) {
                return answer.toString();
            }
        }
        return null;
    }

    /**
     * Gets a Short from a Map in a null-safe manner.
     * <p>
     * The Short is obtained from the results of {@link #getNumber(Map, Object)}.
     *
     * @param map the map to use
     * @param key the key to look up
     * @return the value in the Map as a Short, <code>null</code> if null map input
     */
    public static Short getShort(final Map<?, ?> map, final Object key) {
        Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        } else if (answer instanceof Short) {
            return (Short) answer;
        }
        return answer.shortValue();
    }

    /**
     * Gets a Integer from a Map in a null-safe manner.
     * <p>
     * The Integer is obtained from the results of {@link #getNumber(Map, Object)}.
     *
     * @param map the map to use
     * @param key the key to look up
     * @return the value in the Map as a Integer, <code>null</code> if null map input
     */
    public static Integer getInteger(final Map<?, ?> map, final Object key) {
        Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        } else if (answer instanceof Integer) {
            return (Integer) answer;
        }
        return answer.intValue();
    }

    /**
     * Gets a Long from a Map in a null-safe manner.
     * <p>
     * The Long is obtained from the results of {@link #getNumber(Map, Object)}.
     *
     * @param map the map to use
     * @param key the key to look up
     * @return the value in the Map as a Long, <code>null</code> if null map input
     */
    public static Long getLong(final Map<?, ?> map, final Object key) {
        Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        } else if (answer instanceof Long) {
            return (Long) answer;
        }
        return answer.longValue();
    }

    /**
     * Gets a Float from a Map in a null-safe manner.
     * <p>
     * The Float is obtained from the results of {@link #getNumber(Map, Object)}.
     *
     * @param map the map to use
     * @param key the key to look up
     * @return the value in the Map as a Float, <code>null</code> if null map input
     */
    public static Float getFloat(final Map<?, ?> map, final Object key) {
        Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        } else if (answer instanceof Float) {
            return (Float) answer;
        }
        return answer.floatValue();
    }

    /**
     * Gets a Double from a Map in a null-safe manner.
     * <p>
     * The Double is obtained from the results of {@link #getNumber(Map, Object)}.
     *
     * @param map the map to use
     * @param key the key to look up
     * @return the value in the Map as a Double, <code>null</code> if null map input
     */
    public static Double getDouble(final Map<?, ?> map, final Object key) {
        Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        } else if (answer instanceof Double) {
            return (Double) answer;
        }
        return answer.doubleValue();
    }

    /**
     * Gets a Map from a Map in a null-safe manner.
     * <p>
     * If the value returned from the specified map is not a Map then
     * <code>null</code> is returned.
     *
     * @param map the map to use
     * @param key the key to look up
     * @return the value in the Map as a Map, <code>null</code> if null map input
     */
    public static Map<?, ?> getMap(final Map<?, ?> map, final Object key) {
        if (map != null) {
            Object answer = map.get(key);
            if (answer instanceof Map) {
                return (Map<?, ?>) answer;
            }
        }
        return null;
    }

    /**
     * Gets a Boolean from a Map in a null-safe manner.
     * <p>
     * If the value is a <code>Boolean</code> it is returned directly.
     * If the value is a <code>String</code> and it equals 'true' ignoring case
     * then <code>true</code> is returned, otherwise <code>false</code>.
     * If the value is a <code>Number</code> an integer zero value returns
     * <code>false</code> and non-zero returns <code>true</code>.
     * Otherwise, <code>null</code> is returned.
     *
     * @param map the map to use
     * @param key the key to look up
     * @return the value in the Map as a Boolean, <code>null</code> if null map input
     */
    public static Boolean getBoolean(final Map<?, ?> map, final Object key) {
        if (map != null) {
            Object answer = map.get(key);
            if (answer != null) {
                if (answer instanceof Boolean) {
                    return (Boolean) answer;
                } else if (answer instanceof String) {
                    return Boolean.valueOf((String) answer);
                } else if (answer instanceof Number) {
                    Number n = (Number) answer;
                    return (n.intValue() != 0) ? Boolean.TRUE : Boolean.FALSE;
                }
            }
        }
        return null;
    }

    /**
     * Gets a Number from a Map in a null-safe manner.
     * <p>
     * If the value is a <code>Number</code> it is returned directly.
     * If the value is a <code>String</code> it is converted using
     * {@link NumberFormat#parse(String)} on the system default formatter
     * returning <code>null</code> if the conversion fails.
     * Otherwise, <code>null</code> is returned.
     *
     * @param map the map to use
     * @param key the key to look up
     * @return the value in the Map as a Number, <code>null</code> if null map input
     */
    public static Number getNumber(final Map<?, ?> map, final Object key) {
        if (map != null) {
            Object answer = map.get(key);
            if (answer != null) {
                if (answer instanceof Number) {
                    return (Number) answer;
                } else if (answer instanceof String) {
                    try {
                        String text = (String) answer;
                        return NumberFormat.getInstance().parse(text);

                    } catch (ParseException e) {
                        // failure means null is returned
                    }
                }
            }
        }
        return null;
    }

    /**
     * Gets a short from a Map in a null-safe manner.
     * <p>
     * The short is obtained from the results of {@link #getNumber(Map, Object)}.
     *
     * @param map the map to use
     * @param key the key to look up
     * @return the value in the Map as a short, <code>0</code> if null map input
     */
    public static short getShortValue(final Map<?, ?> map, final Object key) {
        Short shortObject = getShort(map, key);
        if (shortObject == null) {
            return 0;
        }
        return shortObject;
    }

    /**
     * Gets an int from a Map in a null-safe manner.
     * <p>
     * The int is obtained from the results of {@link #getNumber(Map, Object)}.
     *
     * @param map the map to use
     * @param key the key to look up
     * @return the value in the Map as an int, <code>0</code> if null map input
     */
    public static int getIntValue(final Map<?, ?> map, final Object key) {
        Integer integerObject = getInteger(map, key);
        if (integerObject == null) {
            return 0;
        }
        return integerObject;
    }

    /**
     * Gets a long from a Map in a null-safe manner.
     * <p>
     * The long is obtained from the results of {@link #getNumber(Map, Object)}.
     *
     * @param map the map to use
     * @param key the key to look up
     * @return the value in the Map as a long, <code>0L</code> if null map input
     */
    public static long getLongValue(final Map<?, ?> map, final Object key) {
        Long longObject = getLong(map, key);
        if (longObject == null) {
            return 0L;
        }
        return longObject;
    }

    /**
     * Gets a float from a Map in a null-safe manner.
     * <p>
     * The float is obtained from the results of {@link #getNumber(Map, Object)}.
     *
     * @param map the map to use
     * @param key the key to look up
     * @return the value in the Map as a float, <code>0.0F</code> if null map input
     */
    public static float getFloatValue(final Map<?, ?> map, final Object key) {
        Float floatObject = getFloat(map, key);
        if (floatObject == null) {
            return 0f;
        }
        return floatObject;
    }

    /**
     * Gets a double from a Map in a null-safe manner.
     * <p>
     * The double is obtained from the results of {@link #getNumber(Map, Object)}.
     *
     * @param map the map to use
     * @param key the key to look up
     * @return the value in the Map as a double, <code>0.0</code> if null map input
     */
    public static double getDoubleValue(final Map<?, ?> map, final Object key) {
        Double doubleObject = getDouble(map, key);
        if (doubleObject == null) {
            return 0d;
        }
        return doubleObject;
    }

    /**
     * Gets a boolean from a Map in a null-safe manner.
     * <p>
     * If the value is a <code>Boolean</code> its value is returned.
     * If the value is a <code>String</code> and it equals 'true' ignoring case
     * then <code>true</code> is returned, otherwise <code>false</code>.
     * If the value is a <code>Number</code> an integer zero value returns
     * <code>false</code> and non-zero returns <code>true</code>.
     * Otherwise, <code>false</code> is returned.
     *
     * @param map the map to use
     * @param key the key to look up
     * @return the value in the Map as a Boolean, <code>false</code> if null map input
     */
    public static boolean getBooleanValue(final Map<?, ?> map, final Object key) {
        Boolean booleanObject = getBoolean(map, key);
        if (booleanObject == null) {
            return false;
        }
        return booleanObject;
    }
}
