package com.zjh.blog.service;

import com.zjh.blog.dao.pojo.SysUser;
import com.zjh.blog.vo.Result;
import com.zjh.blog.vo.UserVo;

public interface SysUserService {

    SysUser findUserById(Long id);

    SysUser findUser(String account, String password);

    Result findUserByToken(String token);

    SysUser findUserByAccount(String account);

    SysUser checkToken(String token);

    void save(SysUser sysUser);

    UserVo findUserVoById(Long id);
}
