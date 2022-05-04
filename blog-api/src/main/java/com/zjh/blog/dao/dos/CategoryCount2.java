package com.zjh.blog.dao.dos;

import lombok.Data;

//每个月各个分类的发布量
@Data
public class CategoryCount2 {
    private String name;
    private Integer[] data;
}
