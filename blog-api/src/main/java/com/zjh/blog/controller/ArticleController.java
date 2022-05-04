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
    @Cache(expire = 1 * 30 * 1000, name = "list_article")
    public Result listArticle(@RequestBody PageParams pageParams) {
        System.out.println(pageParams);
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
     * 按月归档
     *
     * @return
     */
    @PostMapping("listArchives")
    public Result listArchives() {
        return articleService.listArchives();
    }

    /**
     * 按月和分类归档
     *
     * @return
     */
    @PostMapping("listArchives2")
    public Result listArchives2() {
        return articleService.listArchives2();
    }

    /**
     * 分类统计文章发布数
     * @return
     */
    @PostMapping("categoryCount")
    public Result categoryCount() {
        return articleService.categoryCount();
    }

    /**
     * 分类统计文章浏览量
     * @return
     */
    @PostMapping("viewCount")
    public Result viewCount() {
        return articleService.viewCount();
    }

    /**
     * 文章详情
     * @param articleId
     * @return
     */
    @GetMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId) {
        //System.out.println(articleId);
        return articleService.findArticleById(articleId);
    }

    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam) {
        return articleService.publish(articleParam);
    }

    @GetMapping("author/{authorId}")
    public Result findArticleByAuthorId(@PathVariable("authorId") Long authorId) {
        return articleService.findArticleByAuthorId(authorId);
    }

    @GetMapping("comment/{authorId}")
    public Result findCommentArticle(@PathVariable("authorId") Long authorId) {
        return articleService.findCommentArticle(authorId);
    }

    @PostMapping("delete")
    public Result delete(@RequestBody ArticleParam articleParam) {
        return articleService.delete(articleParam.getId());
    }

    @GetMapping("sensitive")
    public Result getSensitive(){
        return articleService.getSensitive();
    }
}
