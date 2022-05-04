package com.zjh.blog.vo;

import lombok.Data;

@Data
public class SysUserVo {
    private String id;

    private String account;

    private String nickname;

    private String avatar;

    private String createDate;

    private Integer deleted;


}
