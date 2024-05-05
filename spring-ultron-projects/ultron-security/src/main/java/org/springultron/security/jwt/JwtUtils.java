package org.springultron.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.lang.Nullable;
import org.springultron.core.utils.Base64Utils;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.Optional;

/**
 * JWT工具类
 *
 * @author brucewuu
 * @date 2019/10/23 14:48
 */
public final class JwtUtils {

    private JwtUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 生成JWT加密秘钥
     * 默认使用SHA-512算法
     */
    public static String generateMacSecret() {
        return generateMacSecret(Jwts.SIG.HS512);
    }

    /**
     * 生成JWT加密秘钥
     * 支持 SHA-256 SHA-384 SHA-512算法
     *
     * @param algorithm 算法
     */
    public static String generateMacSecret(MacAlgorithm algorithm) {
        SecretKey secretKey = algorithm.key().build();
        return Base64Utils.encodeToString(secretKey.getEncoded());
    }

    /**
     * 生成token的过期时间
     *
     * @param expiration 有效期时长
     */
    @Nullable
    private static Date generateExpirationDate(Duration expiration) {
        return Optional.ofNullable(expiration).map(e -> new Date(System.currentTimeMillis() + e.toMillis())).orElse(null);
    }

    /**
     * 根据username生成JWT
     * 支持 SHA-256 SHA-384 SHA-512算法
     * 默认使用HS512算法
     *
     * @param username 用户名
     * @param secret   签名秘钥
     */
    public static String signWithMac(String username, String secret) {
        return signWithMac(username, secret, Jwts.SIG.HS512);
    }

    /**
     * 根据username生成JWT
     * 支持 SHA-256 SHA-384 SHA-512算法
     * 默认使用HS512算法
     *
     * @param username  用户名
     * @param secret    签名秘钥
     * @param algorithm 算法
     */
    public static String signWithMac(String username, String secret, MacAlgorithm algorithm) {
        byte[] bytes = Base64Utils.decodeFromString(secret);
        SecretKey secretKey = Keys.hmacShaKeyFor(bytes);
        return Jwts.builder().content(username).issuedAt(new Date()).signWith(secretKey, algorithm).compact();
    }

    /**
     * 根据username生成JWT
     * 支持 SHA-256 SHA-384 SHA-512算法
     * 默认使用HS512算法
     *
     * @param username   用户名
     * @param secret     签名秘钥
     * @param expiration 有效期时长(单位:秒)
     */
    public static String signWithMac(String username, String secret, Duration expiration) {
        return signWithMac(username, secret, expiration, Jwts.SIG.HS512);
    }

    /**
     * 根据username生成JWT
     * 支持 SHA-256 SHA-384 SHA-512算法
     * 默认使用HS512算法
     *
     * @param username   用户名
     * @param secret     签名秘钥
     * @param expiration 有效期时长(单位:秒)
     * @param algorithm  算法
     */
    public static String signWithMac(String username, String secret, Duration expiration, MacAlgorithm algorithm) {
        byte[] bytes = Base64Utils.decodeFromString(secret);
        SecretKey secretKey = Keys.hmacShaKeyFor(bytes);
        return Jwts.builder().content(username).issuedAt(new Date()).expiration(generateExpirationDate(expiration)).signWith(secretKey, algorithm).compact();
    }

    /**
     * 生成JWT
     * 支持 SHA-256 SHA-384 SHA-512算法
     * 默认使用HS512算法
     *
     * @param claims jwt负载
     * @param secret 签名秘钥
     */
    public static String signWithMac(Claims claims, String secret) {
        return signWithMac(claims, secret, Jwts.SIG.HS512);
    }

    /**
     * 生成JWT
     *
     * @param claims    jwt负载
     * @param secret    签名秘钥 base64
     * @param algorithm 签名算法,支持 SHA-256 SHA-384 SHA-512算法
     */
    public static String signWithMac(Claims claims, String secret, MacAlgorithm algorithm) {
        byte[] bytes = Base64Utils.decodeFromString(secret);
        SecretKey secretKey = Keys.hmacShaKeyFor(bytes);
        return Jwts.builder().claims(claims).signWith(secretKey, algorithm).compact();
    }

    /**
     * 获取JWT载体
     */
    @Nullable
    public static Claims obtainClaims(String token, String secret) {
        byte[] bytes = Base64Utils.decodeFromString(secret);
        SecretKey secretKey = Keys.hmacShaKeyFor(bytes);
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    /**
     * 获取JWT内容
     */
    public static String obtainContent(String token, String secret) {
        byte[] bytes = Base64Utils.decodeFromString(secret);
        SecretKey secretKey = Keys.hmacShaKeyFor(bytes);
        return new String(Jwts.parser().verifyWith(secretKey).build().parseSignedContent(token).getPayload());
    }

    /**
     * 从JWT中获取userName
     */
    public static String obtainUsername(String token, String secret) {
        return obtainContent(token, secret);
    }

}