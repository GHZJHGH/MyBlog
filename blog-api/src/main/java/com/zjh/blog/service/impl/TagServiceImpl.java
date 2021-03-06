package com.zjh.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjh.blog.dao.mapper.TagMapper;
import com.zjh.blog.dao.pojo.Category;
import com.zjh.blog.dao.pojo.Tag;
import com.zjh.blog.dao.pojo.Tag;
import com.zjh.blog.service.TagService;
import com.zjh.blog.vo.CategoryVo;
import com.zjh.blog.vo.Result;
import com.zjh.blog.vo.TagVo;
import com.zjh.blog.vo.params.TagParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;

    public List<TagVo> copyList(List<Tag> tagList) {
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }

    public TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag, tagVo);
        tagVo.setId(String.valueOf(tag.getId()));
        return tagVo;
    }


    @Override
    public List<TagVo> findTagsByArticleId(Long articleId) {
        //mybatisplus 无法进行多表查询
        List<Tag> tags = tagMapper.findTagsByArticleId(articleId);

        return copyList(tags);
    }

    @Override
    public Result hots(int limit) {
        /**
         * 标签所拥有的文章数最多的为最热标签
         * 查询根据tag_id分组计数，从大到小排列取前limit个
         */
        List<Long> tagIds = tagMapper.findHotsTagIds(limit);
        if (CollectionUtils.isEmpty(tagIds)) {
            return Result.success(Collections.emptyList());
        }
        //查询tagName
        List<Tag> tagList = tagMapper.findTagsByTagIds(tagIds);
        return Result.success(tagList);
    }

    @Override
    public Result findAllTags() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId, Tag::getTagName);
        List<Tag> tags = this.tagMapper.selectList(queryWrapper);
        return Result.success(copyList(tags));
    }

    @Override
    public Result findAllDetail() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getDeleted,0);
        List<Tag> tags = this.tagMapper.selectList(queryWrapper);
        return Result.success(copyList(tags));
    }

    @Override
    public Result findDetailById(Long id) {
        Tag tag = tagMapper.selectById(id);
        return Result.success(copy(tag));
    }

    @Override
    public Result getTagListPage(TagParam tagParam) {
        Page<Tag> page = new Page<>(tagParam.getPage(),5);
        IPage<Tag> tagIPage = tagMapper.listTag(
                page,
                tagParam.getName()
        );
        List<Tag> records = tagIPage.getRecords();

        Integer total = tagMapper.count(tagParam.getName());
        HashMap<String ,Object> map = new HashMap<>();
        List<TagVo> list = copyList(records);
        map.put("data",list);
        map.put("total",total);

        return Result.success(map);
    }

    @Override
    public Result add(Tag tag) {
        tagMapper.insert(tag);
        return Result.success("");
    }

    @Override
    public Result delete(Long id) {
        tagMapper.delete(id);
        return Result.success("");
    }

    @Override
    public Result update(Tag tag) {
        tagMapper.updateById(tag);
        return Result.success("");
    }

    @Override
    public Result batchDelete(String string) {
        String[] ids = string.split(":")[1].replace("}","").replace("\"","").split(",");
        for (int i = 0; i < ids.length; i++) {
            //System.out.println(Long.parseLong(ids[i]));
            tagMapper.delete(Long.parseLong(ids[i]));
        }
        return Result.success("");
    }
}
