package com.zjh.blog.dao.pojo;

import lombok.Data;

@Data
public class User {
    private Long id;

    private String account;

    private String nickname;

    private String avatar;

    private String createDate;

    private String lastLogin;
}
