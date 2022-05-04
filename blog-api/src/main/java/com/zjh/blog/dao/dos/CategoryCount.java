package com.zjh.blog.dao.dos;

import lombok.Data;

//总发布量中各分类的数量
@Data
public class CategoryCount {
    private String name;
    private Integer value;
}
