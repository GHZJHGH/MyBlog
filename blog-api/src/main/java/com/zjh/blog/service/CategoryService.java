package com.zjh.blog.service;

import com.zjh.blog.vo.CategoryVo;
import com.zjh.blog.vo.Result;

public interface CategoryService {

    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result categoryDetailById(Long id);
}
