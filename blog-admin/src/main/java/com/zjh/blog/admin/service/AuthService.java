package com.zjh.blog.admin.service;

import com.zjh.blog.admin.pojo.Admin;
import com.zjh.blog.admin.pojo.Permission;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class AuthService {

    @Autowired
    private AdminService adminService;

    public boolean auth(HttpServletRequest request, Authentication authentication){
        //权限认证
        String requestURI = request.getRequestURI();
        Object principal = authentication.getPrincipal();
        //未登录
        if (principal == null || "anonymousUser".equals(principal)){
            return false;
        }
        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();
        Admin admin = adminService.findAdminByUsername(username);
        if (admin == null){
            return false;
        }
        if (admin.getId() == 1){
            //超级管理员
            return true;
        }
        Long id = admin.getId();
        List<Permission> permissionList = adminService.findPermissionByAdminId(id);
        requestURI = StringUtils.split(requestURI,"?")[0];
        for (Permission permission : permissionList){
            if (requestURI.equals(permission.getPath())){
                return true;
            }
        }
        return true;
    }
}
