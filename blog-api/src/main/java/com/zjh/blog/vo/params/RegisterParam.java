package com.zjh.blog.vo.params;

import lombok.Data;

@Data
public class RegisterParam {
    private String account;

    private String password;

    private String nickname;

    private String avatar;
}
