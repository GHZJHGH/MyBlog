package com.zjh.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjh.blog.dao.pojo.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    IPage<SysUser> listUser(Page<SysUser> page, String name);

    Integer count(String name);

    @Update("update ms_sys_user set deleted = 1 where id = #{id}")
    void delete(Long id);

    @Update("update ms_sys_user set deleted = 0 where id = #{id}")
    void unDelete(Long id);
}
