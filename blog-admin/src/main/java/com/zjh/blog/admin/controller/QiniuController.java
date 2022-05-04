package com.zjh.blog.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.zjh.blog.admin.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("qiniu")
public class QiniuController {

    @GetMapping("/uploadToken")
    public Result qiniuPolicy() {
        String accessKey = "QNXau4hs_zB7-UTkLbE0zVY3WxMdEdf2DK-di3Ws";
        String secretKey = "--_Zuitu0O66tQnWMdjiNqaeocF2epxd-1iVjdLD";
        String bucket = "blog-pic-zjh";
        String path = "http://r81y009bz.hn-bkt.clouddn.com/";
        int expireSeconds = -1 ;

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket,null, expireSeconds, null);

        System.out.println(upToken);
        return Result.success(upToken);
    }
}
