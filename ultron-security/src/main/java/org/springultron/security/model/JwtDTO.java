package org.springultron.security.model;

/**
 * 登陆成功返回 jwt
 *
 * @author brucewuu
 * @date 2019/10/23 12:03
 */
public class JwtDTO {
    private final String token;

    public String getToken() {
        return token;
    }

    private JwtDTO(String token) {
        this.token = token;
    }

    public static JwtDTO of(String token) {
        return new JwtDTO(token);
    }
}