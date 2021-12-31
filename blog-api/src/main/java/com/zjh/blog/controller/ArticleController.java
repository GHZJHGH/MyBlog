package com.zjh.blog.controller;

import com.zjh.blog.common.aop.LogAnnotation;
import com.zjh.blog.common.cache.Cache;
import com.zjh.blog.service.ArticleService;
import com.zjh.blog.vo.Result;
import com.zjh.blog.vo.params.ArticleParam;
import com.zjh.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    /**
     * 首页文章列表
     *
     * @param pageParams
     * @return
     */
    @PostMapping
    @LogAnnotation(module = "文章", operator = "获取文章列表")
    @Cache(expire = 5 * 60 * 1000, name = "list_article")
    public Result listArticle(@RequestBody PageParams pageParams) {
        return articleService.listArticle(pageParams);
    }

    /**
     * 最热文章
     *
     * @return
     */
    @PostMapping("hot")
    @Cache(expire = 5 * 60 * 1000, name = "hot_article")
    public Result hotArticle() {
        int limit = 5;
        return articleService.hotArticle(limit);
    }

    /**
     * 最新文章
     *
     * @return
     */
    @PostMapping("new")
    @Cache(expire = 5 * 60 * 1000, name = "news_article")
    public Result newArticle() {
        int limit = 5;
        return articleService.newArticle(limit);
    }

    /**
     * 最新文章
     *
     * @return
     */
    @PostMapping("listArchives")
    public Result listArchives() {
        return articleService.listArchives();
    }

    @GetMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId) {
        System.out.println(articleId);
        return articleService.findArticleById(articleId);
    }

    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam) {
        return articleService.publish(articleParam);
    }

}
