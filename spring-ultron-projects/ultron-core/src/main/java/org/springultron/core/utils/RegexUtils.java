package org.springultron.core.utils;

import org.springultron.core.pool.PatternPool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具
 *
 * @author brucewuu
 * @date 2019-06-06 18:06
 */
public class RegexUtils {

    private RegexUtils() {
    }

    /**
     * 验证手机号码
     *
     * @param phone 手机号码
     * @return {boolean}
     */
    public static boolean matchPhone(String phone) {
        return isMatch(phone, PatternPool.MOBILE);
    }

    /**
     * 验证用户名(允许6-16字符，允许字母数字下划线)
     *
     * @param userName 用户名
     * @return {boolean}
     */
    public static boolean matchUserName(String userName) {
        return isMatch(userName, "^[a-zA-Z0-9]\\w{5,15}$");
    }

    /**
     * 验证昵称
     *
     * @param nickName 用户名
     * @return {boolean}
     */
    public static boolean matchNickName(String nickName) {
        return isMatch(nickName, "^[a-zA-Z\\u4E00-\\u9FA5][a-zA-Z0-9_\\u4E00-\\u9FA5]{1,11}$");
    }

    /**
     * 验证邮箱地址
     *
     * @param email 邮箱地址
     * @return {boolean}
     */
    public static boolean matchEmail(String email) {
        return isMatch(email, PatternPool.EMAIL);
    }

    /**
     * 验证密码是否合法
     * 长度在6~18之间，只能包含字母、数字和下划线
     *
     * @param password 密码
     * @return {boolean}
     */
    public static boolean matchPassword(String password) {
        return isMatch(password, "^[a-zA-Z0-9]\\w{5,17}$");
    }

    /**
     * 给定内容是否匹配正则
     *
     * @param content 内容
     * @param pattern 模式
     */
    public static boolean isMatch(CharSequence content, Pattern pattern) {
        if (content == null || pattern == null) {
            return false;
        }
        return pattern.matcher(content).matches();
    }

    /**
     * 编译传入正则表达式和字符串去匹配,忽略大小写
     *
     * @param content 内容
     * @param regex   正则
     * @return {boolean}
     */
    public static boolean isMatch(CharSequence content, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
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
    public static String findResult(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
