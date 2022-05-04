package com.zjh.blog.dao.pojo;

import lombok.Data;

@Data
public class Tag {

    private Long id;

    private String avatar;

    private String tagName;

    private Byte deleted;

}
