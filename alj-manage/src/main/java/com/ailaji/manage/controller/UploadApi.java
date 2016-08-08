package com.ailaji.manage.controller;

import java.io.InputStream;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ailaji.manage.bean.user.PicUploadResult;
import com.ailaji.manage.utils.FileUtils;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

@Controller
@RequestMapping("upload")
public class UploadApi {

    Logger logger = Logger.getLogger(UploadApi.class);

    @Autowired
    private OSSClient ossClient;

    @Autowired
    private String bucketName;

    @Autowired
    private String endpoint;

    // 允许上传的格式
    private static final String[] IMAGE_TYPE = new String[] { ".bmp", ".jpg", ".jpeg", ".gif", ".png" };

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<PicUploadResult> upload(@RequestParam("uploadFile") MultipartFile uploadFile,
            HttpServletResponse response) {
        PicUploadResult result = new PicUploadResult();
        try {
            Long contentLength = uploadFile.getSize();
            InputStream inputStream = uploadFile.getInputStream();
            String originalFilename = uploadFile.getOriginalFilename();
            // 创建上传Object的Metadata
            ObjectMetadata meta = new ObjectMetadata();
            meta.setCacheControl("no-cache");
            // 必须设置ContentLength
            meta.setContentLength(contentLength);
            // 校验图片格式
            boolean isLegal = false;

            String contentType = "";
            for (String type : IMAGE_TYPE) {
                if (StringUtils.endsWithIgnoreCase(originalFilename, type)) {
                    isLegal = true;
                    contentType = FileUtils.contentType(type);
                    break;
                }
            }
            meta.setContentType(contentType);

            /*BufferedImage image = ImageIO.read(inputStream);
            if (image != null) {
                result.setWidth(image.getWidth() + "");
                result.setHeight(image.getHeight() + "");
                isLegal = true;
            } else {
                isLegal = false;
            }
            if (!isLegal) {
                result.setError(1);

            } else {*/
                String fileName = new DateTime(new Date()).toString("yyyyMMddhhmmssSSSS")
                        + RandomUtils.nextInt(100, 9999) + "."
                        + StringUtils.substringAfterLast(originalFilename, ".");
                //AliOssUtils.putObject("ailaji", contentLength, contentType, fileName, inputStream, -1);

                PutObjectResult putObject = ossClient.putObject(bucketName, fileName, inputStream, meta);
                System.out.println("错误日志:"+putObject.getETag());
                String imgUrl = String.format("%s/%s", endpoint, fileName);
                result.setUrl(imgUrl);
            

        } catch (Exception e) {
            String errMsg = String.format("%s", "upload image failed");
            logger.error(errMsg + e);
        }
        return ResponseEntity.ok(result);
    }

}
