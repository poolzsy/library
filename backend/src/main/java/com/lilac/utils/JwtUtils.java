package com.lilac.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

/**
 * JWT工具类
 *
 * @author lilac
 */
public class JwtUtils {

    // 有效期为24小时
    public static final Long JWT_TTL = 24 * 60 * 60 * 1000L;
    // 设置秘钥明文
    public static final String JWT_KEY = "lilac-library-secret-key-for-jwt-token-generation-123456";

    /**
     * 获取UUID
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     */
    public static String createJWT(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());
        return builder.compact();
    }

    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     */
    public static String createJWT(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());
        return builder.compact();
    }

    /**
     * 生成jwt
     *
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);
        return builder.compact();
    }

    /**
     * 生成jwt的builder对象
     * @param subject token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     */
    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = JwtUtils.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)
                .setSubject(subject)
                .setIssuer("lilac")
                .setIssuedAt(now)
                .signWith(secretKey)
                .setExpiration(expDate);
    }

    /**
     * 生成加密后的秘钥 secretKey
     */
    public static SecretKey generalKey() {
        byte[] keyBytes = JWT_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 解析
     *
     */
    public static Claims parseJWT(String jwt) {
        SecretKey secretKey = generalKey();
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        // 使用新方法创建一个示例token
        String subject = "{\"userId\":\"123456\"}";
        String token = createJWT(subject);
        System.out.println("Generated Token: " + token);

        // 解析token
        try {
            Claims claims = parseJWT(token);
            System.out.println("Parsed Claims: " + claims);
            System.out.println("Subject from claims: " + claims.getSubject());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
