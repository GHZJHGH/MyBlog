package com.zjh.blog.admin.service;

import com.zjh.blog.admin.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class SecurityUserService implements UserDetailsService {

    @Autowired
    private AdminService adminService;

    /**
     * 登录的时候，会把username传到这里
     * 通过username查询admin表，如果admin存在，则将密码告诉spring security
     * 如果不存在则返回null,认证失败
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = this.adminService.findAdminByUsername(username);
        if (admin == null){
            return null;
        }
        UserDetails userDetails = new User(username,admin.getPassword(),new ArrayList<>());
        return userDetails;
    }
}
