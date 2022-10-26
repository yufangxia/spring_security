package com.yf.Server.Imp;

import com.yf.Server.loginServer;
import com.yf.domain.LoginUser;
import com.yf.domain.ResponseResult;
import com.yf.domain.User;
import com.yf.utils.JwtUtil;
import com.yf.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class loginServerImp implements loginServer {
    @Autowired
    //获取security链中的authManager 用其的 authenticationManager.authenticate 来进行用户登录认证
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        //登录认证
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authentication);
        //认证失败
        if (Objects.isNull(authenticate)) {
            throw new SecurityException("登陆失败");
        }
        //认证成功
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String uuid = loginUser.getUser().getId().toString();
        //使用uuid生成jwt, 并存储到redis中
        String jwt = JwtUtil.createJWT(uuid);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);

        redisCache.setCacheObject("login:"+uuid, loginUser);
        return new  ResponseResult(200, "登录成功", map);
    }

    @Override
    public ResponseResult logout() {
        //从SecurityContextHolder中获取userid
        long id = Thread.currentThread().getId();
        log.info("正在为线程:{}, SecurityContextHolder.get", id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();

        //从redis中删除数据
        redisCache.deleteObject("login:"+userid);

        return new ResponseResult(200, "注销成功");
    }
}
