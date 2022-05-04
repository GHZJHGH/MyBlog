package com.zjh.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.zjh.blog.dao.pojo.SysUser;
import com.zjh.blog.service.LoginService;
import com.zjh.blog.service.SysUserService;
import com.zjh.blog.utils.JWTUtils;
import com.zjh.blog.vo.ErrorCode;
import com.zjh.blog.vo.Result;
import com.zjh.blog.vo.params.LoginParam;
import com.zjh.blog.vo.params.RegisterParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String salt = "mszlu!@#";

    @Override
    public Result login(LoginParam loginParam) {
        /**
         * 检查参数是否合法
         * 根据用户名和密码去user表中查询是否存在
         * 如果不存在，则登陆失败
         * 如果存在，则检查deleted是否为1
         * 如果为0，则使用jwt生成token返回给前端
         * 将token放入redis中,token:user信息 并设置过期时间
         * (登录认证的时候，先认证token是否合法，去redis认证是否存在)
         */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        password = DigestUtils.md5Hex(password + salt);
        SysUser sysUser = sysUserService.findUser(account, password);
        if (sysUser == null) {
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }

        if (sysUser.getDeleted()==1){
            return Result.fail(ErrorCode.IS_DELETE.getCode(), ErrorCode.IS_DELETE.getMsg());
        }
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser), 1, TimeUnit.DAYS);
        return Result.success(token);
    }


    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_" + token);
        return Result.success(null);
    }

    @Override
    public Result register(RegisterParam registerParam) {
        /**
         * 1.判断参数 是否合法
         * 2.判断账户是否存在，存在 返回账户已经被注册
         * 3.不存在，注册用户
         * 4.生成token
         * 5.存入redis 并返回
         * 6.注意 加上事务，一旦中间的任何过程出现问题，注册的用户 需要回滚
         */
        String account = registerParam.getAccount();
        String password = registerParam.getPassword();
        String nickname = registerParam.getNickname();
        String avatar = registerParam.getAvatar();
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password) || StringUtils.isBlank(nickname) || StringUtils.isBlank(avatar)) {
            return Result.fail((ErrorCode.PARAMS_ERROR.getCode()), ErrorCode.PARAMS_ERROR.getMsg());
        }
        SysUser sysUser = sysUserService.findUserByAccount(account);
        if (sysUser != null) {
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        SysUser sysUser1 = new SysUser();
        sysUser1.setNickname(nickname);
        sysUser1.setAccount(account);
        sysUser1.setPassword(DigestUtils.md5Hex(password + salt));
        sysUser1.setCreateDate(System.currentTimeMillis());
        sysUser1.setLastLogin(System.currentTimeMillis());
        sysUser1.setAvatar(avatar);
        sysUser1.setAdmin(1); //1 为true
        sysUser1.setDeleted(0); // 0 为false
        sysUser1.setSalt("");
        sysUser1.setStatus("");
        sysUser1.setEmail("");

        this.sysUserService.save(sysUser1);
        String token = JWTUtils.createToken(sysUser1.getId());
        redisTemplate.opsForValue().set("TOKEN_" + token, JSON.toJSONString(sysUser1), 1, TimeUnit.DAYS);

        return Result.success(token);
    }
}
