package org.springultron.core.utils;

/**
 * @author brucewuu
 * @date 2019-06-06 11:58
 */
public class Hex {

    private Hex() {
    }

    private static final char[] DIGITS_LOWER;
    private static final char[] DIGITS_UPPER;

    static {
        DIGITS_LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        DIGITS_UPPER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    }

    public static byte[] decodeHex(final String data) {
        return decodeHex(data.toCharArray());
    }

    public static byte[] decodeHex(final char[] data) {
        final int len = data.length;

        if ((len & 0x01) != 0) {
            throw new IllegalArgumentException("Odd number of characters.");
        }

        final byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    public static String encodeHexString(byte[] data) {
        return new String(encodeHex(data));
    }

    public static String encodeHexString(byte[] data, boolean toLowerCase) {
        return new String(encodeHex(data, toLowerCase));
    }

    public static char[] encodeHex(byte[] data) {
        return encodeHex(data, true);
    }

    public static char[] encodeHex(byte[] data, boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    private static char[] encodeHex(byte[] data, char[] toDigits) {
        int l = data.length;
        char[] out = new char[l << 1];
        int i = 0;

        for (int var5 = 0; i < l; ++i) {
            out[var5++] = toDigits[(240 & data[i]) >>> 4];
            out[var5++] = toDigits[15 & data[i]];
        }

        return out;
    }

    protected static int toDigit(final char ch, final int index) {
        final int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new IllegalArgumentException("Illegal hexadecimal character " + ch + " at index " + index);
        }
        return digit;
    }

//    /**
//     * byte数组转化为16进制字符串
//     *
//     * @param bytes byte数组
//     * @return String
//     */
//    public static String byteToHexString(byte[] bytes) {
//        StringBuilder sb = new StringBuilder();
//        for (byte aByte : bytes) {
//            String strHex = Integer.toHexString(aByte);
//            if (strHex.length() > 3) {
//                sb.append(strHex.substring(6));
//            } else {
//                if (strHex.length() < 2) {
//                    sb.append("0").append(strHex);
//                } else {
//                    sb.append(strHex);
//                }
//            }
//        }
//        return sb.toString();
//    }
}
