package com.zjh.blog.dao.dos;

import lombok.Data;

//每个月各分类的数量
@Data
public class Archives2 {
    private Integer year;
    private Integer month;
    private String name;
    private Long count;
}
