package com.zjh.blog.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjh.blog.admin.pojo.Admin;
import com.zjh.blog.admin.pojo.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

    @Select("SELECT * FROM ms_permission where id in (select permission_id from ms_admin_permission where admin_id=#{adminId})")
    List<Permission> findPermissionByAdminId(Long adminId);
}
