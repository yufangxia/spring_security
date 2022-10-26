package com.yf.Server;


import com.yf.domain.ResponseResult;
import com.yf.domain.User;

public interface loginServer {
    ResponseResult login(User user);

    ResponseResult logout();
}
