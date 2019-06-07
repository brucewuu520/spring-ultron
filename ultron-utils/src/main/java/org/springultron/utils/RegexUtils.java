package org.springultron.utils;

import org.springframework.lang.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具
 *
 * @Auther: brucewuu
 * @Date: 2019-06-06 18:06
 * @Description:
 */
public class RegexUtils {

    /**
     * 用户名
     */
    private static final String USER_NAME = "^[a-zA-Z\\u4E00-\\u9FA5][a-zA-Z0-9_\\u4E00-\\u9FA5]{1,11}$";

    /**
     * 手机号
     */
    private static final String PHONE = "^1[23456789]\\d{9}$";

    /**
     * 邮箱
     */
    private static final String EMAIL = "^\\w+([-+.]*\\w+)*@([\\da-z](-[\\da-z])?)+(\\.{1,2}[a-z]+)+$";

    /**
     * 验证手机号码
     *
     * @param phone 手机号码
     * @return {boolean}
     */
    public static boolean matchPhone(String phone) {
        return match(phone, PHONE);
    }

    /**
     * 验证用户名
     *
     * @param userName 用户名
     * @return {boolean}
     */
    public static boolean matchUserName(String userName) {
        return match(userName, USER_NAME);
    }

    /**
     * 验证邮箱地址
     *
     * @param email 邮箱地址
     * @return {boolean}
     */
    public static boolean matchEmail(String email) {
        return match(email, EMAIL);
    }

    /**
     * 验证手机号或者邮箱
     *
     * @param text 手机号或者邮箱
     * @return {boolean}
     */
    public static boolean matchPhoneOrEmail(String text) {
        return match(text, EMAIL + "|" + PHONE);
    }

    /**
     * 编译传入正则表达式和字符串去匹配,忽略大小写
     *
     * @param text  字符串
     * @param regex 正则
     * @return {boolean}
     */
    public static boolean match(String text, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    /**
     * 编译传入正则表达式在字符串中寻找，如果匹配到则为true
     *
     * @param text  字符串
     * @param regex 正则
     * @return {boolean}
     */
    public static boolean find(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    /**
     * 编译传入正则表达式在字符串中寻找，如果找到返回第一个结果
     * 找不到返回null
     *
     * @param text  字符串
     * @param regex 正则
     * @return {boolean}
     */
    @Nullable
    public static String findResult(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
