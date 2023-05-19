package org.springultron.security;

import io.jsonwebtoken.JwtException;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 获取用户详情处理
 *
 * @author brucewuu
 * @date 2020/6/14 14:04
 */
public interface UserDetailsProcessor extends UserDetailsService {
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
     * 用户退出登录
     *
     * @param username 用户名
     */
    void logout(String username);
}
