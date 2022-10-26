package com.yf.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @PreAuthorize("hasAuthority('system:test')")
    @GetMapping("/hello")
    public String getHello() {
        return "hello";
    }
}
