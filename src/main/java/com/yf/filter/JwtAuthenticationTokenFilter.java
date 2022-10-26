package com.yf.filter;

import com.yf.domain.LoginUser;
import com.yf.utils.JwtUtil;
import com.yf.utils.RedisCache;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uuid;
        //判断token是否合法
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //token不存在
            filterChain.doFilter(request, response);
            return ;
        }
        //解析token
        try {
            Claims claims = JwtUtil.parseJWT(token);
            uuid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }

        //从redis中读取user
        String redisKey = "login:" + uuid;
        LoginUser loginUser = redisCache.getCacheObject(redisKey);
        if (Objects.isNull(loginUser)) {
            //todo 这里应该使用全局异常捕获 然后跳转至登录界面 
            throw new RuntimeException("用户未登录");
        }
        //封存入SecurityContextHolder
        //TODO 获取权限封装到authentication的第三个参数中
        Collection<? extends GrantedAuthority> authorities = loginUser.getAuthorities();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUser, null, authorities);
        //将authentication 存入 SecurityContextHolder 中
        long id = Thread.currentThread().getId();
        log.info("正在为线程：{}，SecurityContextHolder.set", id);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
