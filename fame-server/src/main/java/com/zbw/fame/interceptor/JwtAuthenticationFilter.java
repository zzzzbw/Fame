package com.zbw.fame.interceptor;

import com.zbw.fame.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * web 配置
 *
 * @author zzzzbw
 * @since 2021/10/25 14:22
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain) throws ServletException, IOException {

        String header = req.getHeader(JwtUtil.JWT_HEADER_KEY);
        if (StringUtils.hasText(header)) {
            String jwtToken = header.replace("Bearer", "");

            String username = JwtUtil.getSubject(jwtToken);
            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(JwtUtil.getRoles(jwtToken));

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        filterChain.doFilter(req, resp);
    }
}
