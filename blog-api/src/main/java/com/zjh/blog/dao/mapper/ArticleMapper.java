package com.zjh.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjh.blog.dao.dos.Archives;
import com.zjh.blog.dao.dos.Archives2;
import com.zjh.blog.dao.dos.CategoryCount;
import com.zjh.blog.dao.pojo.Article;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    List<Archives> listArchives();

    IPage<Article> listArticle(Page<Article> page, Long categoryId, Long tagId, String year, String month,String title);

    @Select("SELECT * FROM ms_article\n" +
            "WHERE id in(\n" +
            "\tSELECT DISTINCT article_id FROM `ms_comment`\n" +
            "\tWHERE author_id = #{author_id}\n" +
            ")")
    List<Article> findCommentArticle(Long author_id);

    List<CategoryCount> categoryCount();

    List<CategoryCount> viewCount();

    @Delete("delete from ms_article_body where article_id = #{id}")
    Integer delete_Body(Long id);

    @Delete("delete from ms_article_tag where article_id = #{id}")
    Integer delete_tags(Long id);

    String[] getName();

    List<Archives2> listArchives2();
}
