package com.yf.Server.Imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yf.domain.LoginUser;
import com.yf.domain.User;
import com.yf.mapper.MenuMapper;
import com.yf.mapper.UserMapper;
import com.yf.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        //查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userName);
        User user = userMapper.selectOne(queryWrapper);
        //如果未找到用户
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        //显示当前用户线程
        long id = Thread.currentThread().getId();
        log.info("当前用户线程为{}", id);
        //TODO 根据用户查询权限信息 添加到LoginUser中
//        List<String> permissions = new  ArrayList<>(Arrays.asList("user", "test"));
        List<String> permissions = menuMapper.selectPermsByUserId(user.getId());
        return new LoginUser(user, permissions);
    }
}






















