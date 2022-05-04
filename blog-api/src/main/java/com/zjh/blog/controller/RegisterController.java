package com.zjh.blog.controller;

import com.zjh.blog.service.LoginService;
import com.zjh.blog.vo.Result;
import com.zjh.blog.vo.params.LoginParam;
import com.zjh.blog.vo.params.RegisterParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("register")
public class RegisterController {
    @Autowired
    private LoginService loginService;

    @PostMapping
    public Result register(@RequestBody RegisterParam registerParam) {
        //sso 单点登录，后期如果把登录注册功能 提出去（单独的服务，可以独立提供接口服务）
        return loginService.register(registerParam);
    }
}
