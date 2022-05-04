package com.zjh.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjh.blog.dao.pojo.Category;
import com.zjh.blog.dao.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {


    /**
     * 根据文章id查询标签列表
     *
     * @param articleId
     * @return
     */
    List<Tag> findTagsByArticleId(Long articleId);

    List<Long> findHotsTagIds(int limit);

    List<Tag> findTagsByTagIds(List<Long> tagIds);

    IPage<Tag> listTag(Page<Tag> page, String name);

    Integer count(String name);

    @Update("update ms_tag set deleted = 1 where id = #{id}")
    void delete(Long id);
}
