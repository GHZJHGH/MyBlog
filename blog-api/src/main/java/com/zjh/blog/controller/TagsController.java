package com.zjh.blog.controller;


import com.zjh.blog.dao.pojo.Tag;
import com.zjh.blog.service.TagService;
import com.zjh.blog.vo.Result;
import com.zjh.blog.vo.params.TagParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//标签
@RestController
@RequestMapping("tags")
public class TagsController {

    @Autowired
    private TagService tagService;

    @GetMapping("hot")
    public Result hot() {
        int limit = 5;
        return tagService.hots(limit);
    }

    @GetMapping
    public Result findAll() {
        return tagService.findAllTags();
    }

    @GetMapping("detail")
    public Result findAllDetail() {
        return tagService.findAllDetail();
    }

    @GetMapping("detail/{id}")
    public Result findDetailById(@PathVariable("id") Long id) {
        return tagService.findDetailById(id);
    }


    @GetMapping("listpage")
    public Result getTagListPage(TagParam tagParam){
        System.out.println(tagParam);
        return tagService.getTagListPage(tagParam);
    }
    @PostMapping("add")
    public Result addTag(@RequestBody Tag tag) {
        return tagService.add(tag);
    }
    @PostMapping("delete")
    public Result delete(@RequestBody Tag tag){
        return tagService.delete(tag.getId());
    }
    @PostMapping("update")
    public Result update(@RequestBody Tag tag){
        System.out.println(tag);
        return tagService.update(tag);
    }
    @PostMapping("batchDelete")
    public Result batchDelete(@RequestBody String string){
        return tagService.batchDelete(string);
    }
}
