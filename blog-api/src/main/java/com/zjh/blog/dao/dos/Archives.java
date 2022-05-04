package com.zjh.blog.dao.dos;

import lombok.Data;

//不需要持久化到数据库的对象
//按月归档
@Data
public class Archives {
    private Integer year;
    private Integer month;
    private Long count;
}
