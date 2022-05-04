package com.zjh.blog.controller;

import com.zjh.blog.dao.pojo.SysUser;
import com.zjh.blog.dao.pojo.User;
import com.zjh.blog.service.SysUserService;
import com.zjh.blog.vo.Result;
import com.zjh.blog.vo.params.UserParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token) {
        return sysUserService.findUserByToken(token);
    }


    @GetMapping("listpage")
    public Result getUserListPage(UserParam userParam){
        System.out.println(userParam);
        return sysUserService.getUserListPage(userParam);
    }
    @PostMapping("add")
    public Result addUser(@RequestBody SysUser user) {
        return sysUserService.add(user);
    }
    @PostMapping("delete")
    public Result delete(@RequestBody SysUser user){
        return sysUserService.delete(user.getId());
    }
    @PostMapping("unDelete")
    public Result undelete(@RequestBody SysUser user){
        return sysUserService.unDelete(user.getId());
    }

    @PostMapping("update")
    public Result update(@RequestBody SysUser user){
        System.out.println(user);
        user.setCreateDate(null);
        return sysUserService.update(user);
    }
    @PostMapping("batchDelete")
    public Result batchDelete(@RequestBody String string){
        return sysUserService.batchDelete(string);
    }

}
