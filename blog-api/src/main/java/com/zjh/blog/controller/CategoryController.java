package com.zjh.blog.controller;

import com.zjh.blog.dao.pojo.Category;
import com.zjh.blog.service.CategoryService;
import com.zjh.blog.vo.Result;
import com.zjh.blog.vo.params.CategoryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

//分类
@RestController
@RequestMapping("categorys")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public Result categories() {
        return categoryService.findAll();
    }

    @GetMapping("detail")
    public Result categoriesDetail() {
        return categoryService.findAllDetail();
    }

    @GetMapping("detail/{id}")
    public Result categoryDetailById(@PathVariable("id") Long id) {
        return categoryService.categoryDetailById(id);
    }

    @GetMapping("listpage")
    public Result getCategoryListPage(CategoryParam categoryParam){
        System.out.println(categoryParam);
        return categoryService.getCategoryListPage(categoryParam);
    }
    @PostMapping("add")
    public Result addCategory(@RequestBody Category category) {
        return categoryService.add(category);
    }
    @PostMapping("delete")
    public Result delete(@RequestBody Category category){
        return categoryService.delete(category.getId());
    }
    @PostMapping("update")
    public Result update(@RequestBody Category category){
        return categoryService.update(category);
    }
    @PostMapping("batchDelete")
    public Result batchDelete(@RequestBody String string){
        return categoryService.batchDelete(string);
    }
}
