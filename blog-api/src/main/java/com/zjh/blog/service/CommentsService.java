package com.zjh.blog.service;


import com.zjh.blog.vo.Result;
import com.zjh.blog.vo.params.CommentParam;

public interface CommentsService {

    /**
     * 根据文章Id查询评论
     *
     * @param id
     * @return
     */
    Result commentsByArticleId(Long id);

    Result comment(CommentParam commentParam);
}
