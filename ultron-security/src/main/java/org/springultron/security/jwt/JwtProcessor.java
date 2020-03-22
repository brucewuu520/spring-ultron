package org.springultron.security.jwt;

import io.jsonwebtoken.JwtException;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springultron.security.model.UserInfo;

/**
 * jwt解析处理器
 *
 * @author brucewuu
 * @date 2020/1/9 13:56
 */
public interface JwtProcessor {
    /**
     * 根据用户名生成用户的jwt
     *
     * @param username 用户名
     * @return jwt
     */
    String generateToken(String username);

    /**
     * 从jwt中获取username
     *
     * @param jwt jwt字符串
     * @return username
     */
    @Nullable
    String obtainUsername(String jwt) throws JwtException;

    /**
     * 根据用户名获取 {@link UserDetails}
     *
     * @param username 用户名
     * @return {@link UserDetails}
     */
    @Nullable
    UserDetails getUserByUsername(String username);

    /**
     * 用户退出登录
     *
     * @param userInfo 用户信息
     */
    void logout(UserInfo userInfo);
}