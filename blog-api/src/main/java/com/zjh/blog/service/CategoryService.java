package com.zjh.blog.service;

import com.zjh.blog.dao.pojo.Category;
import com.zjh.blog.vo.CategoryVo;
import com.zjh.blog.vo.Result;
import com.zjh.blog.vo.params.CategoryParam;

public interface CategoryService {

    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result categoryDetailById(Long id);

    Result getCategoryListPage(CategoryParam categoryParam);

    Result add(Category category);

    Result delete(Long id);

    Result update(Category category);

    Result batchDelete(String string);
}
