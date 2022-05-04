package com.zjh.blog.admin.controller;

import com.zjh.blog.admin.vo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @PostMapping("login")
    public Result login(){
        System.out.println(1111);
        return Result.success(null);
    }
}
