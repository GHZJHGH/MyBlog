package com.zjh.blog.controller;

import com.zjh.blog.utils.QiniuUtils;
import com.zjh.blog.vo.ErrorCode;
import com.zjh.blog.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("upload")
public class UploadController {
    @Autowired
    private QiniuUtils qiniuUtils;

    @PostMapping
    public Result upload(@RequestParam("image") MultipartFile file) {
        System.out.println("-------------------------------------------");
        //原始文件名称
        String originalFilename = file.getOriginalFilename();
        //文件名称
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(originalFilename, ".");
        //上传文件到七牛云
        boolean upload = qiniuUtils.upload(file, fileName);
        System.out.println(upload);
        if (upload) {
            System.out.println(QiniuUtils.url + fileName);
            return Result.success(QiniuUtils.url + fileName);
        } else {
            System.out.println("上传失败");
            return Result.fail(ErrorCode.UPLOAD_ERROR.getCode(), ErrorCode.UPLOAD_ERROR.getMsg());
        }
    }
}
