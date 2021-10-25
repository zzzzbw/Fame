package com.zbw.fame.util;

import cn.hutool.core.map.MapUtil;
import io.jsonwebtoken.Claims;
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
     * 有效期12小时
     */
    private final long EXPIRE_TIME = 60 * 60 * 1000 * 12;


    /**
     * 生成令牌
     *
     * @return 令牌
     */
    public String generateToken(String subject, String roles, Map<String, String> additional) {
        Map<String, Object> claims = new HashMap<>(3);
        claims.put(SUBJECT, subject);
        claims.put(CREATED, new Date());
        claims.put(AUTHORITIES, roles);
        if (MapUtil.isNotEmpty(additional)) {
            claims.putAll(additional);
        }

        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
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
        return Jwts.parserBuilder()
                .deserializeJsonWith(new JacksonDeserializer<>(FameUtils.getObjectMapper()))
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();

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
}
