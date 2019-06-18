package org.springultron.core.utils;

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
     * 验证手机号码
     *
     * @param phone 手机号码
     * @return {boolean}
     */
    public static boolean matchPhone(String phone) {
        return match(phone, "^1[23456789]\\d{9}$");
    }

    /**
     * 验证用户名(字母开头，允许5-16字符，允许字母数字下划线)
     *
     * @param userName 用户名
     * @return {boolean}
     */
    public static boolean matchUserName(String userName) {
        return match(userName, "^[a-zA-Z][a-zA-Z0-9_]{4,15}$");
    }

    /**
     * 验证昵称
     *
     * @param nickName 用户名
     * @return {boolean}
     */
    public static boolean matchNickName(String nickName) {
        return match(nickName, "^[a-zA-Z\\u4E00-\\u9FA5][a-zA-Z0-9_\\u4E00-\\u9FA5]{1,11}$");
    }

    /**
     * 验证邮箱地址
     *
     * @param email 邮箱地址
     * @return {boolean}
     */
    public static boolean matchEmail(String email) {
        return match(email, "^\\w+([-+.]*\\w+)*@([\\da-z](-[\\da-z])?)+(\\.{1,2}[a-z]+)+$");
    }

    /**
     * 验证手机号或者邮箱
     *
     * @param text 手机号或者邮箱
     * @return {boolean}
     */
    public static boolean matchPhoneOrEmail(String text) {
        return match(text, "(^1[23456789]\\d{9}$)|(^\\w+([-+.]*\\w+)*@([\\da-z](-[\\da-z])?)+(\\.{1,2}[a-z]+)+$)");
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
