package com.zjh.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjh.blog.dao.mapper.CategoryMapper;
import com.zjh.blog.dao.pojo.Category;
import com.zjh.blog.service.CategoryService;
import com.zjh.blog.vo.CategoryVo;
import com.zjh.blog.vo.Result;
import com.zjh.blog.vo.params.CategoryParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryVo findCategoryById(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        categoryVo.setId(String.valueOf(category.getId()));
        return categoryVo;
    }

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Category::getId, Category::getCategoryName);
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        //页面交互的对象
        return Result.success(copyList(categories));
    }

    @Override
    public Result findAllDetail() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getDeleted,0);
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        //页面交互的对象
        return Result.success(copyList(categories));
    }

    @Override
    public Result categoryDetailById(Long id) {
        Category category = categoryMapper.selectById(id);
        return Result.success(copy(category));
    }

    @Override
    public Result getCategoryListPage(CategoryParam categoryParam) {
        Page<Category> page = new Page<>(categoryParam.getPage(),5);
        IPage<Category> categoryIPage = categoryMapper.listCategory(
                page,
                categoryParam.getName()
        );
        List<Category> records = categoryIPage.getRecords();

        Integer total = categoryMapper.count(categoryParam.getName());
        HashMap<String ,Object> map = new HashMap<>();
        List<CategoryVo> list = copyList(records);
        map.put("data",list);
        map.put("total",total);

        return Result.success(map);
    }

    @Override
    public Result add(Category category) {
        categoryMapper.insert(category);
        return Result.success("");
    }

    @Override
    public Result delete(Long id) {
        categoryMapper.delete(id);
        return Result.success("");
    }

    @Override
    public Result update(Category category) {
        categoryMapper.updateById(category);
        return Result.success("");
    }

    @Override
    public Result batchDelete(String string) {
        String[] ids = string.split(":")[1].replace("}","").replace("\"","").split(",");
        for (int i = 0; i < ids.length; i++) {
            //System.out.println(Long.parseLong(ids[i]));
            categoryMapper.delete(Long.parseLong(ids[i]));
        }
        return Result.success("");
    }

    public CategoryVo copy(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        categoryVo.setId(String.valueOf(category.getId()));
        return categoryVo;
    }

    public List<CategoryVo> copyList(List<Category> categoryList) {
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category : categoryList) {
            categoryVoList.add(copy(category));
        }
        return categoryVoList;
    }
}
