package org.springultron.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.lang.Nullable;
import org.springframework.util.Base64Utils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

/**
 * JWT工具类
 *
 * @Author: brucewuu
 * @Date: 2019/10/23 14:48
 */
public class JwtUtils {
    private JwtUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 生成JWT加密秘钥
     * 默认使用SHA-512算法
     */
    public static String generateSecret() {
        return generateSecret(SignatureAlgorithm.HS512);
    }

    /**
     * 生成JWT加密秘钥
     * 支持 SHA-256 SHA-384 SHA-512算法
     *
     * @param algorithm 算法
     */
    public static String generateSecret(SignatureAlgorithm algorithm) {
        SecretKey secretKey = Keys.secretKeyFor(algorithm);
        return Base64Utils.encodeToString(secretKey.getEncoded());
    }

    /**
     * 生成JWT
     * 默认使用HS512算法
     *
     * @param claims jwt负载
     * @param secret 签名秘钥
     */
    public static String generateToken(Claims claims, String secret) {
        return generateToken(claims, secret, SignatureAlgorithm.HS512);
    }

    /**
     * 生成JWT
     *
     * @param claims    jwt负载
     * @param secret    签名秘钥
     * @param algorithm 签名算法,支持 SHA-256 SHA-384 SHA-512算法
     */
    public static String generateToken(Claims claims, String secret, SignatureAlgorithm algorithm) {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
        Key signKey = new SecretKeySpec(apiKeySecretBytes, algorithm.getJcaName());
        return Jwts.builder()
                .setClaims(claims)
                .signWith(signKey, algorithm)
                .compact();
    }

    /**
     * 从JWT中获取userName
     */
    @Nullable
    public static String obtainUsername(String token, String secret) {
        return Optional.ofNullable(obtainClaims(token, secret)).map(Claims::getAudience).orElse(null);
    }

    /**
     * 获取JWT载体
     */
    @Nullable
    public static Claims obtainClaims(String token, String secret) {
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secret)).parseClaimsJws(token).getBody();
    }

    /**
     * 生成token的过期时间
     *
     * @param expiration 有效期时长(ms)
     */
    @Nullable
    private static Date generateExpirationDate(Long expiration) {
        return Optional.ofNullable(expiration).map(e -> new Date(System.currentTimeMillis() + e * 1000)).orElse(null);
    }
}