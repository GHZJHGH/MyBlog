package com.zjh.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjh.blog.dao.dos.Archives;
import com.zjh.blog.dao.dos.Archives2;
import com.zjh.blog.dao.dos.CategoryCount;
import com.zjh.blog.dao.dos.CategoryCount2;
import com.zjh.blog.dao.mapper.ArticleBodyMapper;
import com.zjh.blog.dao.mapper.ArticleMapper;
import com.zjh.blog.dao.mapper.ArticleTagMapper;
import com.zjh.blog.dao.pojo.Article;
import com.zjh.blog.dao.pojo.ArticleBody;
import com.zjh.blog.dao.pojo.ArticleTag;
import com.zjh.blog.dao.pojo.SysUser;
import com.zjh.blog.service.ArticleService;
import com.zjh.blog.service.CategoryService;
import com.zjh.blog.service.SysUserService;
import com.zjh.blog.service.TagService;
import com.zjh.blog.utils.UserThreadLocal;
import com.zjh.blog.vo.ArticleBodyVo;
import com.zjh.blog.vo.ArticleVo;
import com.zjh.blog.vo.Result;
import com.zjh.blog.vo.TagVo;
import com.zjh.blog.vo.params.ArticleParam;
import com.zjh.blog.vo.params.PageParams;
import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ThreadService threadService;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());

        IPage<Article> articleIPage = articleMapper.listArticle(
                page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth(),
                pageParams.getTitle());

        List<Article> records = articleIPage.getRecords();
        return Result.success(copyList(records, true, true));
    }

//    @Override
//    public Result listArticle(PageParams pageParams) {
//        /**
//         * 分页查询article数据库表
//         */
//        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSisze());
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        if (pageParams.getCategoryId() != null){
//            //and Article.Category_id = #{categoryId}
//            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//        }
//        List<Long> articleIdList = new ArrayList<>();
//        if (pageParams.getTagId() != null){
//            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
//            for (ArticleTag articleTag : articleTags){
//                articleIdList.add(articleTag.getArticleId());
//            }
//            if (articleIdList.size() > 0){
//                //and id in (1,2)
//                queryWrapper.in(Article::getId,articleIdList);
//            }
//        }
//
//        //是否置顶进行排序
//        //创建时间排序
//        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
//        List<Article> records = articlePage.getRecords();
//        List<ArticleVo> articleVoList = copyList(records,true,true);
//        return Result.success(articleVoList);
//    }

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //select id,title from article order by view_counts desc limit 5
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);

        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles, false, false));
    }

    @Override
    public Result newArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //select id,title from article order by create_date desc limit 5
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);

        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles, false, false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();

        return Result.success(archivesList);
    }

    @Override
    public Result listArchives2() {
        List<Archives> archivesList = articleMapper.listArchives();
        String[] category_name = articleMapper.getName();
        List<Archives2> list = articleMapper.listArchives2();

        HashMap hashMap = new HashMap();
        String[] date = new String[archivesList.size()];
        Long[] total = new Long[archivesList.size()];
        int sum = 0;
        for (int i = 0; i < archivesList.size(); i++) {
            date[i] = archivesList.get(i).getYear().toString() + "年" + archivesList.get(i).getMonth().toString() + "月";
            total[i] = archivesList.get(i).getCount();
            sum += archivesList.get(i).getCount();
        }

        CategoryCount2[] count2s = new CategoryCount2[category_name.length];
        for (int i = 0; i < category_name.length; i++) {
            int[] count = new int[date.length];
            count2s[i] = new CategoryCount2();
            count2s[i].setName(category_name[i]);

            for (int j = 0; j < date.length; j++) {
                for (Archives2 value : list) {
                    if (value.getYear().equals(archivesList.get(j).getYear()) &&
                            value.getMonth().equals(archivesList.get(j).getMonth()) &&
                            value.getName().equals(category_name[i])) {
                        count[j] = Math.toIntExact(value.getCount());
                    }
                }
            }
            count2s[i].setData(Arrays.stream(count).boxed().toArray(Integer[]::new));
        }

        hashMap.put("date", date);
        hashMap.put("data", count2s);
        hashMap.put("name", category_name);
        hashMap.put("total", total);
        hashMap.put("sum", sum);

        return Result.success(hashMap);
    }

    @Override
    public Result findArticleById(Long articleId) {
        /**
         * 1.根据id查询文章信息
         * 2.根据bodyId和categoryId去做关联查询
         */
        Article article = articleMapper.selectById(articleId);
        ArticleVo articleVo = copy(article, true, true, true, true);

        //查看完文章了，更新增阅读数，带来的问题
        //查看完文章之后，本应该直接返回数据了，这时候做了一个更新操作，更新时加写锁，阻塞其他的读操作，性能就会比较低
        //更新操作增加了此次接口的耗时 如果一旦更新出问题，会影响查看文章的操作
        //解决方法：用线程池可以把更新操作扔到线程池中去执行，和主线程就不相关了
        threadService.updateArticleViewCount(articleMapper, article);
        return Result.success(articleVo);
    }

    @Override
    public Result publish(ArticleParam articleParam) {
        /**
         * 1.构建Article对象
         * 2.获取作者id(将接口加入到拦截中才能保证发布者已登录)
         * 3.标签
         * 4.body，内容
         */
        //authorId
        SysUser sysUser = UserThreadLocal.get();
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setWeight(Article.Article_Common);
        article.setViewCounts(0);
        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setCategoryId(Long.valueOf(articleParam.getCategory().getId()));

        //插入后会生成文章id
        this.articleMapper.insert(article);
        //tag
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                Long articleId = article.getId();
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(Long.valueOf(tag.getId()));
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }
        }
        //body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);

        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);

        String p = "{\"page\":1,\"pageSize\":5}";
        p = DigestUtils.md5Hex(p);
        String key = "list_article" + "::" + "ArticleController" + "::" + "listArticle" + "::" + p;
        redisTemplate.delete(key);

        Map<String, String> map = new HashMap<>();
        map.put("id", article.getId().toString());
        return Result.success(map);
    }

    @Override
    public Result findArticleByAuthorId(Long authorId) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getAuthorId, authorId);
        List<Article> article = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(article, true, true));
    }

    @Override
    public Result findCommentArticle(Long authorId) {
        List<Article> article = articleMapper.findCommentArticle(authorId);
        return Result.success(copyList(article, true, true));
    }

    @Override
    public Result categoryCount() {
        List<CategoryCount> archivesList = articleMapper.categoryCount();
        return Result.success(archivesList);
    }

    @Override
    public Result viewCount() {
        List<CategoryCount> archivesList = articleMapper.viewCount();
        return Result.success(archivesList);
    }

    @Override
    @Transactional
    public Result delete(Long id) {
        int i = articleMapper.delete_Body(id);
        int j = articleMapper.delete_tags(id);
        int row = articleMapper.deleteById(id);
        System.out.println(i);
        System.out.println(j);
        System.out.println(row);
        if (i == 0 || j == 0 || row == 0) {
            try {
                throw new Exception(String.valueOf(1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Result.success("");
    }

    @Override
    public Result getSensitive() {
        ClassPathResource classPathResource = new ClassPathResource("sensitive/adv.txt");

        try (InputStream inputStream = classPathResource.getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));) {

            ArrayList<String> arrayList = new ArrayList();
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                arrayList.add(str);
            }
            return Result.success(arrayList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.fail(5000, "读取失败");
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, false, false));
        }
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record, isTag, isAuthor, isBody, isCategory));
        }
        return articleVoList;
    }

    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setId(String.valueOf(article.getId()));

        //月份是大写的MM,分钟是小写的mm
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        if (isTag) {
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if (isAuthor) {
            Long authorId = article.getAuthorId();
            articleVo.setAuthorId(String.valueOf(authorId));
            SysUser sysUser = sysUserService.findUserById(authorId);
            articleVo.setAuthor(sysUser.getNickname());
            articleVo.setAvatar(sysUser.getAvatar());
        }
        if (isBody) {
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory) {
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }
}
