package com.yf.controller;

import com.yf.Server.loginServer;
import com.yf.domain.ResponseResult;
import com.yf.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    private loginServer loginServer;

    @PostMapping("/user/login")

    public ResponseResult login(@RequestBody User user) {

        return loginServer.login(user);
    }

    @PostMapping("/user/logout")
    public ResponseResult logout() {

        return loginServer.logout();
    }
}
