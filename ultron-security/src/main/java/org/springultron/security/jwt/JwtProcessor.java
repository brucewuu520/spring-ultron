package org.springultron.security.jwt;

import io.jsonwebtoken.JwtException;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * jwt解析处理器
 *
 * @author brucewuu
 * @date 2020/1/9 13:56
 */
public interface JwtProcessor {
    /**
     * 从jwt中获取username
     *
     * @param jwt jwt字符串
     * @return username
     */
    @Nullable
    String obtainUsername(String jwt) throws JwtException;

    @Nullable
    UserDetails getUserByUsername(String username);

    String generateToken(String username);
}
