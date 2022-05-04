package com.zjh.blog.service;

import com.zjh.blog.vo.Result;
import com.zjh.blog.vo.params.LoginParam;
import com.zjh.blog.vo.params.RegisterParam;

public interface LoginService {
    /**
     * 登录功能
     *
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);

    /**
     * 退出登录
     *
     * @param token
     * @return
     */
    Result logout(String token);

    /**
     * 注册
     *
     * @param loginParam
     * @return
     */
    Result register(RegisterParam registerParam);
}
