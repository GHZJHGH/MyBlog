package com.zjh.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjh.blog.dao.mapper.SysUserMapper;
import com.zjh.blog.dao.pojo.SysUser;
import com.zjh.blog.dao.pojo.Tag;
import com.zjh.blog.dao.pojo.User;
import com.zjh.blog.service.SysUserService;
import com.zjh.blog.utils.JWTUtils;
import com.zjh.blog.vo.*;
import com.zjh.blog.vo.params.UserParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            sysUser.setNickname("zjzz");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account);
        queryWrapper.eq(SysUser::getPassword, password);
        queryWrapper.select(SysUser::getAccount, SysUser::getId, SysUser::getAvatar, SysUser::getNickname, SysUser::getDeleted);
        queryWrapper.last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public Result findUserByToken(String token) {
        /**
         * 1.token合法性校验,是否为空，解析是否成功，redis是否存在
         * 2.如果校验失败，返回错误
         * 3.如果成功，返回对应的结果LoginUserVo
         */
        SysUser sysUser = checkToken(token);
        if (sysUser == null) {
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
        LoginUserVo loginUserVo = new LoginUserVo();
        loginUserVo.setId(String.valueOf(sysUser.getId()));
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setNickname(sysUser.getNickname());
        loginUserVo.setAvatar(sysUser.getAvatar());

        return Result.success(loginUserVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount, account);
        queryWrapper.last("limit 1");
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        return sysUser;
    }

    @Override
    public void save(SysUser sysUser) {
        //id会自动生成，mybatis-plus默认生成的id是分布式id,通过雪花算法生成
        this.sysUserMapper.insert(sysUser);
    }

    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("zjzz");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser, userVo);
        userVo.setId(String.valueOf(sysUser.getId()));
        return userVo;
    }

    @Override
    public SysUser checkToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        Map<String, Object> map = JWTUtils.checkToken(token);
        if (map == null) {
            return null;
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(userJson)) {
            return null;
        }
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }

    @Override
    public Result getUserListPage(UserParam userParam) {
        Page<SysUser> page = new Page<>(userParam.getPage(),5);
        IPage<SysUser> userIPage = sysUserMapper.listUser(
                page,
                userParam.getName()
        );
        List<SysUser> records = userIPage.getRecords();

        Integer total = sysUserMapper.count(userParam.getName());
        HashMap<String ,Object> map = new HashMap<>();
        List<SysUserVo> list = copyList(records);
        map.put("data",list);
        map.put("total",total);

        return Result.success(map);
    }

    @Override
    public Result add(SysUser user) {
        sysUserMapper.insert(user);
        return Result.success("");
    }

    @Override
    public Result delete(Long id) {
        sysUserMapper.delete(id);
        return Result.success("");
    }

    @Override
    public Result update(SysUser user) {
        sysUserMapper.updateById(user);
        return Result.success("");
    }

    @Override
    public Result batchDelete(String string) {
        String[] ids = string.split(":")[1].replace("}","").replace("\"","").split(",");
        for (int i = 0; i < ids.length; i++) {
            //System.out.println(Long.parseLong(ids[i]));
            sysUserMapper.delete(Long.parseLong(ids[i]));
        }
        return Result.success("");
    }

    @Override
    public Result unDelete(Long id) {
        sysUserMapper.unDelete(id);
        return Result.success("");
    }

    public List<SysUserVo> copyList(List<SysUser> userList) {
        List<SysUserVo> userVoList = new ArrayList<>();
        for (SysUser sysUser : userList) {
            userVoList.add(copy(sysUser));
        }
        return userVoList;
    }

    public SysUserVo copy(SysUser sysUser) {
        SysUserVo sysUserVo = new SysUserVo();
        BeanUtils.copyProperties(sysUser, sysUserVo);
        sysUserVo.setId(String.valueOf(sysUser.getId()));

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        String creatDate = dateformat.format(sysUser.getCreateDate());
//        String lastLogin = dateformat.format(sysUser.getLastLogin());

        sysUserVo.setCreateDate(creatDate);
//        sysUserVo.setLastLogin(lastLogin);

        return sysUserVo;
    }
}
