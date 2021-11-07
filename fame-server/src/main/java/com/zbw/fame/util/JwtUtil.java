package com.zbw.fame.util;

import cn.hutool.core.map.MapUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.jackson.io.JacksonSerializer;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * web security 配置
 *
 * @author zzzzbw
 * @since 2021/10/25 16:04
 */
@UtilityClass
public class JwtUtil {


    public final String JWT_HEADER_KEY = HttpHeaders.AUTHORIZATION;
    public final String JWT_HEADER_TOKEN_HEAD_KEY = "Bearer";


    private final String SUBJECT = Claims.SUBJECT;

    /**
     * 创建时间
     */
    private final String CREATED = "created";
    /**
     * 权限列表
     */
    private final String AUTHORITIES = "authorities";

    /**
     * 密钥
     */
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    /**
     * token 有效期： 1小时
     */
    private final long TOKEN_EXPIRE_TIME = 60 * 60 * 1000;

    /**
     * refreshToken 有效期： token有效期倍数
     */
    private final long REFRESH_TOKEN_EXPIRE_TIME = TOKEN_EXPIRE_TIME * 5;


    /**
     * 生成令牌
     *
     * @return 令牌
     */
    public String generateToken(String subject, String roles, Map<String, String> additional) {
        return generateToken(subject, roles, additional, TOKEN_EXPIRE_TIME);
    }

    /**
     * 生成 refreshToken
     *
     * @return refreshToken
     */
    public String generateRefreshToken(String subject, String roles, Map<String, String> additional) {
        return generateToken(subject, roles, additional, REFRESH_TOKEN_EXPIRE_TIME);
    }


    private String generateToken(String subject, String roles, Map<String, String> additional, long expireTime) {
        Map<String, Object> claims = new HashMap<>(3);
        claims.put(SUBJECT, subject);
        claims.put(CREATED, new Date());
        claims.put(AUTHORITIES, roles);
        if (MapUtil.isNotEmpty(additional)) {
            claims.putAll(additional);
        }

        Date expirationDate = new Date(System.currentTimeMillis() + expireTime);
        return Jwts.builder()
                .serializeToJsonWith(new JacksonSerializer<>(FameUtils.getObjectMapper()))
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 从令牌中解析出数据
     *
     * @param jwtToken
     * @return
     */
    public Claims getClaims(String jwtToken) {
        try {
            return Jwts.parserBuilder()
                    .deserializeJsonWith(new JacksonDeserializer<>(FameUtils.getObjectMapper()))
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * 从令牌中解析出subject
     *
     * @param jwtToken
     * @return
     */
    public String getSubject(String jwtToken) {
        return getClaims(jwtToken).getSubject();
    }

    /**
     * 从令牌中解析出角色权限
     *
     * @param jwtToken
     * @return
     */
    public String getRoles(String jwtToken) {
        return getClaims(jwtToken).get(AUTHORITIES, String.class);
    }

    /**
     * 从令牌中解析出创建时间
     *
     * @param jwtToken
     * @return
     */
    public Date getCreated(String jwtToken) {
        return getClaims(jwtToken).get(CREATED, Date.class);
    }

    /**
     * token 是否过期
     *
     * @param token
     * @return
     */
    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }
}
