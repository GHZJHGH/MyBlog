package com.zjh.blog.service;

import com.zjh.blog.dao.pojo.Tag;
import com.zjh.blog.vo.Result;
import com.zjh.blog.vo.TagVo;
import com.zjh.blog.vo.params.TagParam;

import java.util.List;

public interface TagService {
    List<TagVo> findTagsByArticleId(Long articleId);

    Result hots(int limit);

    Result findAllTags();

    Result findAllDetail();

    Result findDetailById(Long id);

    Result getTagListPage(TagParam tagParam);

    Result add(Tag tag);

    Result delete(Long id);

    Result update(Tag tag);

    Result batchDelete(String string);
}
