package com.zjh.blog.service;

import com.zjh.blog.vo.Result;
import com.zjh.blog.vo.TagVo;

import java.util.List;

public interface TagService {
    List<TagVo> findTagsByArticleId(Long articleId);

    Result hots(int limit);

    Result findAllTags();

    Result findAllDetail();

    Result findDetailById(Long id);
}
