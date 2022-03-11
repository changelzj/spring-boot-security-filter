package com.example.controller;

import com.example.util.R;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("user")
public class UserController {

    /**
     * 获取登录的用户
     */
    @RequestMapping("test")
    public R test() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return R.ok("test");
    }

    /**
     * 获取登录的用户
     */
    @RequestMapping("test2")
    public R test2(Authentication authentication) {
        return R.ok("test");
    }

}





