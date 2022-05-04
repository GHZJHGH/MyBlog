package com.zjh.blog.service;

import com.zjh.blog.dao.pojo.SysUser;
import com.zjh.blog.dao.pojo.Tag;
import com.zjh.blog.dao.pojo.User;
import com.zjh.blog.vo.Result;
import com.zjh.blog.vo.UserVo;
import com.zjh.blog.vo.params.UserParam;

public interface SysUserService {

    SysUser findUserById(Long id);

    SysUser findUser(String account, String password);

    Result findUserByToken(String token);

    SysUser findUserByAccount(String account);

    SysUser checkToken(String token);

    void save(SysUser sysUser);

    UserVo findUserVoById(Long id);

    Result getUserListPage(UserParam userParam);

    Result add(SysUser user);

    Result delete(Long id);

    Result update(SysUser user);

    Result batchDelete(String string);


    Result unDelete(Long id);
}
