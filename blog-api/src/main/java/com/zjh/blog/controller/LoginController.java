package com.zjh.blog.controller;


import com.zjh.blog.dao.pojo.SysUser;
import com.zjh.blog.service.LoginService;
import com.zjh.blog.utils.UserThreadLocal;
import com.zjh.blog.vo.Result;
import com.zjh.blog.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("login")
    public Result login(@RequestBody LoginParam loginParam) {
        return loginService.login(loginParam);
    }

    @GetMapping("logout")
    public Result logout(@RequestHeader("Authorization") String token) {
        return loginService.logout(token);
    }

    @GetMapping("test")
    public Result test() {
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        return Result.success(null);
    }
}
