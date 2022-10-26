package com.yf;

import com.yf.domain.User;
import com.yf.mapper.MenuMapper;
import com.yf.mapper.UserMapper;
import org.jetbrains.annotations.TestOnly;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootTest

public class MapperTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void getTest() {
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }

    @Test
    public void testEncoding() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        System.out.println(bCryptPasswordEncoder.encode("123456"));
    }

    @Autowired
    private MenuMapper menuMapper;

    @Test
    public void selectPermsByUserId() {
        List<String> strings = menuMapper.selectPermsByUserId(1L);
        System.out.println(strings);
    }
}
