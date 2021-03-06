package com.zjh.blog.service;

import com.zjh.blog.vo.Result;
import com.zjh.blog.vo.params.ArticleParam;
import com.zjh.blog.vo.params.PageParams;

public interface ArticleService {

    /**
     * 分页查询文章列表
     *
     * @param pageParams
     * @return
     */
    Result listArticle(PageParams pageParams);

    /**
     * 最热文章
     *
     * @param limit
     * @return
     */
    Result hotArticle(int limit);

    /**
     * 最新文章
     *
     * @param limit
     * @return
     */
    Result newArticle(int limit);

    /**
     * 文章归档
     *
     * @return
     */
    Result listArchives();

    Result listArchives2();

    Result findArticleById(Long articleId);

    Result publish(ArticleParam articleParam);

    Result findArticleByAuthorId(Long authorId);

    Result findCommentArticle(Long authorId);

    Result categoryCount();

    Result viewCount();

    Result delete(Long id);

    Result getSensitive();
}
